package vn.hieu4tuoi.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import vn.hieu4tuoi.common.OpenAIToolProvider;
import vn.hieu4tuoi.common.RoleChat;
import vn.hieu4tuoi.common.ToolCallType;
import vn.hieu4tuoi.dto.request.chatbot.ChatRequest;
import vn.hieu4tuoi.dto.request.chatbot.UserChatRequest;
import vn.hieu4tuoi.dto.request.chatbot.ChatbotRequest;
import vn.hieu4tuoi.dto.request.order.OrderByAIRequest;
import vn.hieu4tuoi.dto.request.order.OrderItemByAIRequest;
import vn.hieu4tuoi.dto.respone.chat.AIResponse;
import vn.hieu4tuoi.dto.respone.food.FoodDetailResponse;
import vn.hieu4tuoi.dto.respone.invoice.InvoiceResponse;
import vn.hieu4tuoi.dto.respone.order.OrderResponse;
import vn.hieu4tuoi.exception.ResourceNotFoundException;
import vn.hieu4tuoi.model.Customer;
import vn.hieu4tuoi.model.Function;
import vn.hieu4tuoi.model.ToolCall;
import vn.hieu4tuoi.repository.ChatHistoryRepository;
import vn.hieu4tuoi.repository.CustomerRepository;
import vn.hieu4tuoi.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CHAT_BOT_SERVICE")
public class ChatBotServiceImpl implements ChatBotService {
    @Value("${openai.api.key}")
    private String apiKey;
    @Value("${openai.api.url}")
    private String apiUrl;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final FoodService foodService;
    private final CustomerRepository customerRepository;
    private final ChatHistoryService chatHistoryService;
    private final ChatHistoryRepository chatHistoryRepository;
    private final OpenAIToolProvider toolListProvider;
    private final OrderService orderService;
    private final InvoiceService invoiceService;
    //lay thong tin thanh toan tu properites
    @Value("${bank.api.account-number}")
    private String accountNumber;
    private String branch = "Chi nhánh Hà Nội";
    private String bank = "MBBank";
    private String name = "Phạm Huy Tuấn";

    @Transactional
    public String getChatResponse(UserChatRequest req) {
        if (req.getContent() == null || req.getContent().isEmpty()) {
            throw new RuntimeException("Nội dung không được để trống");
        }
        //kiêm tra customer ton tai
        Customer customer = customerRepository.findById(req.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Khách hàng không tồn tại")
        );

        String systemPrompt = """
                    Bạn là một trợ lý gợi ý món ăn, chỉ trả lời thông tin liên quan đến nhà hàng. NẾU khách hàng chưa tìm được món ăn phù hợp và cần tư vấn, KHÔNG tư vấn ngay mà cần dẫn dắt câu chuyện để hỏi được đầy đủ thông tin từ khách hàng như: "muốn ăn gì", "ngân sách", "số lượng người", "khẩu vị (cay, vừa)".
                          - NẾU khách hàng đặt nhiều hơn 1 món thì xác nhận lại thông tin tên món và số lượng trước khi gọi hàm Order, nếu chỉ gọi 1 món ( có thể gọi nhiều số lượng) thì không cần xác nhận lại thông tin.
                """;

        //lay ds lich su chat trong ngay cua customer
        List<ChatbotRequest.Message> chatHistories = chatHistoryService.getRecentChatHistoies(req.getCustomerId()).stream().map(
                chatHistoryResponse -> {
                    return ChatbotRequest.Message.builder()
                            .role(chatHistoryResponse.getRole())
                            .content(chatHistoryResponse.getContent())
                            .tool_calls(chatHistoryResponse.getToolCalls())
                            .tool_call_id(chatHistoryResponse.getToolCallId())
                            .build();
                }
        ).collect(Collectors.toList());

        List<ChatbotRequest.Message> messages = new ArrayList<>();
        messages.add(ChatbotRequest.Message.builder()
                .role("system")
                .content(systemPrompt)
                .build());
        //them lich su chat vao request
        for (ChatbotRequest.Message chatHistory : chatHistories) {
            messages.add(ChatbotRequest.Message.builder()
                    .role(chatHistory.getRole())
                    .content(chatHistory.getContent())
                    .tool_calls(chatHistory.getTool_calls().isEmpty() ? null : chatHistory.getTool_calls())
                    .tool_call_id(chatHistory.getTool_call_id())
                    .build());
        }
        //luu lich su chat user
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setContent(req.getContent());
        chatRequest.setRole(RoleChat.user);
        chatRequest.setCustomerId(req.getCustomerId());
        chatRequest.setToolCallId(null);
        chatRequest.setToolCalls(null);
        chatHistoryService.saveAndFlush(chatRequest);

        messages.add(ChatbotRequest.Message.builder()
                .role("user")
                .content(req.getContent())
                .build());

        // 3. Gọi API
        return CallAIAPi(messages, req);
    }

    @Transactional
    private String CallAIAPi(List<ChatbotRequest.Message> messages, UserChatRequest req) {
        // 3. Thiết lập headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        //chuan bi request
        ChatbotRequest request = ChatbotRequest.builder()
                .model("gpt-4.1")
                .messages(messages)
                .tools(toolListProvider.getToolList())
                .tool_choice("auto")
                .max_tokens(500)
                .temperature(0.7)
                .top_p(0.85)
                .build();
        HttpEntity<ChatbotRequest> entity = new HttpEntity<>(request, headers);
        // 4. Gọi API
        try {
            ResponseEntity<AIResponse> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    AIResponse.class
            );

            // 5. Xử lý response
            if (response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    !response.getBody().getChoices().isEmpty()) {
                AIResponse.Choice.Message messageResponseFromAI = response.getBody().getChoices().get(0).getMessage();
                //neu co tool call thi goi ham
                if (messageResponseFromAI.getTool_calls() != null && !messageResponseFromAI.getTool_calls().isEmpty()) {
                    //neu tool la get ds food
                    String toolName = messageResponseFromAI.getTool_calls().get(0).getFunction().getName();
                    if (toolName.equals("Get_All_Food")) {
                        //goi ham lay ds food
                        List<FoodDetailResponse> allFood = foodService.getAllFood();
                        //format ds food
                        String foodListStringFormat = allFood.stream().map(food -> food.toJson()).collect(Collectors.joining(", "));
                        //chuyển đổi danh sách món ăn thành định dạng JSON
                        String foodListJson = "[" + foodListStringFormat + "]";
                        return handleFunctionCall(messages, req, messageResponseFromAI, "Danh sách món ăn gồm: " + foodListJson);
                    }
                    //neu tool la order
                    else if (toolName.equals("Order")) {
                        //chuyen đôi arguments từ json sang mảng orderByAIRequest
                        List<OrderItemByAIRequest> orderItemByAIRequestList = new ArrayList<>();
                        try {
                            String arguments = messageResponseFromAI.getTool_calls().get(0).getFunction().getArguments();
                            // Parse the JSON wrapper first
                            OrderByAIRequest orderWrapper = objectMapper.readValue(arguments, OrderByAIRequest.class);
                            // Then get the items from the wrapper
                            if (orderWrapper != null && orderWrapper.getItems() != null) {
                                orderItemByAIRequestList = orderWrapper.getItems();
                            }
                        } catch (Exception e) {
                            log.error("Lỗi khi chuyển đổi arguments: {}", e.getMessage());
                            return "Lỗi khi chuyển đổi dữ liệu đặt hàng.";
                        }
                        //goi ham order
                        Long orderId = orderService.saveOrderByAI(OrderByAIRequest.builder()
                                .customerId(req.getCustomerId())
                                .diningTableId(req.getDiningTableId())
                                .items(orderItemByAIRequestList)
                                .build());
                        log.info("Order ID: {}", orderId);
                        return handleFunctionCall(messages, req, messageResponseFromAI, "Đặt hàng thành công");
                    }
                    //neu ham ham lay ds order
                    else if (toolName.equals("Get_Order_List")) {
                        //lay ds order cua ban an
                        List<OrderResponse> orderResponseList = orderService.getOrderByDiningTableId(req.getDiningTableId());
                        String orderListJsonFormat = orderResponseList.stream().map(OrderResponse::toJson).collect(Collectors.joining(", ", "[", "]"));
                        log.info("Json orderList:" + orderListJsonFormat);
                        return handleFunctionCall(messages, req, messageResponseFromAI, "Gửi thông tin đơn hàng đến cho khách hàng. Trường hợp có món nào chưa kịp giao và đang ở trạng thái PENDING thì không tính tiền món đó nhưng báo cho khách hàng có thể đợi nấu xong để giao. Gửi lại cho khách hàng thông tin hóa đơn gồm ( món ăn x số lương x giá, tổng tiền) - Mỗi món ăn 1 hàng. Danh sách đơn hàng gồm: " + orderListJsonFormat);
                    }
                    //neu la ham pay(thanh toan)
                    else if (toolName.equals("Pay")) {
                        String arguments = messageResponseFromAI.getTool_calls().get(0).getFunction().getArguments();
                        String paymentMethod = null;
                        try {
                            JsonNode root = objectMapper.readTree(arguments);
                            if (root.has("payment_method")) {
                                paymentMethod = root.get("payment_method").asText();
                            }
                        } catch (Exception e) {
                            log.error("Lỗi khi parse JSON arguments: {}", e.getMessage(), e);
                            return "Lỗi khi chuyển đổi dữ liệu thanh toán.";
                        }
                        if (paymentMethod.equals("bank")) {
                            //lay thong tin hoa don cua ban hien tai
                            InvoiceResponse invoiceResponse = invoiceService.getCurrentTableInvoice(req.getDiningTableId());
                            String qrCodeUrl = String.format("https://qr.sepay.vn/img?acc=%s&bank=%s&amount=%s&des=%s&template=qronly&download=DOWNLOAD", accountNumber, bank, invoiceResponse.getTotalPrice(), invoiceResponse.getId());
                            return handleFunctionCall(messages, req, messageResponseFromAI, "BẮT BUỘC PHẢI gi JSON object này đến khách hàng, KHÔNG gửi thêm thông tin gì khác {\"message\": \"Vui lòng quét mã QR để thanh toán\", \"imgQRCode\": \"" + qrCodeUrl + "\"}");
                        } else if (paymentMethod.equals("cash")) {
                            return handleFunctionCall(messages, req, messageResponseFromAI, "Nhắc khách hàng ra quầy để thanh toán");
                        } else {
                            return handleFunctionCall(messages, req, messageResponseFromAI, "Phương thức thanh toán không hợp lệ.");
                        }
                    }
                } else {
                    //neu không có tool call thì trả về tin nhắn bình thường
                    //luu doan chat assistant vao db
                    ChatRequest chatAssistantRequest = new ChatRequest();
                    chatAssistantRequest.setContent(messageResponseFromAI.getContent());
                    chatAssistantRequest.setRole(RoleChat.assistant);
                    chatAssistantRequest.setCustomerId(req.getCustomerId());
                    chatAssistantRequest.setToolCallId(null);
                    chatAssistantRequest.setToolCalls(null);
                    chatHistoryService.saveAndFlush(chatAssistantRequest);
                    String messageResponse = messageResponseFromAI.getContent();
                    return messageResponse;
                }
            } else {
                // Xử lý lỗi từ API
                log.error("Lỗi từ API: {}", response.getStatusCode());
                return "Xin lỗi, không thể xử lý yêu cầu của bạn lúc này.";
            }
        } catch (Exception e) {
            // Xử lý lỗi
            throw new RuntimeException("Lỗi khi gọi OPEN AI API: " + e.getMessage(), e);
        }
        return "Xin lỗi, không thể xử lý yêu cầu của bạn lúc này.";
    }

    //hàm xử lý function call để tránh lặp code
    private String handleFunctionCall(List<ChatbotRequest.Message> messages, UserChatRequest req, AIResponse.Choice.Message messageResponseFromAI, String contentToolCall) {
        //tin nhan yêu cầu gọi hàm từ AI
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setContent(null);
        chatRequest.setRole(RoleChat.assistant);
        chatRequest.setCustomerId(req.getCustomerId());
        chatRequest.setToolCallId(null);
        //convert tu request cua AI sang request dung de gọi ham save chatHistory
        List<ToolCall> toolCallRequestList = new ArrayList<>();
        for (AIResponse.Choice.ToolCall toolCallResponseFromAI : messageResponseFromAI.getTool_calls()) {
            ToolCall toolCallRequest = new ToolCall();
            toolCallRequest.setId(toolCallResponseFromAI.getId());
            toolCallRequest.setType(ToolCallType.function);
            toolCallRequest.setFunction(Function.builder()
                    .name(toolCallResponseFromAI.getFunction().getName())
                    .arguments(toolCallResponseFromAI.getFunction().getArguments())
                    .build());
            toolCallRequestList.add(toolCallRequest);
        }
        chatRequest.setToolCalls(toolCallRequestList);
        //luu doan chat yeu cau goi ham vao db
        chatHistoryService.saveAndFlush(chatRequest);

        //add yêu cầu gọi hàm từ AI vào lịch sử chat
        ChatbotRequest.Message MessageOfFunctionCallRequestFromAI = ChatbotRequest.Message.builder()
                .role("assistant")
                .content(null)
                .tool_calls(toolCallRequestList)
                .tool_call_id(null)
                .build();
        messages.add(MessageOfFunctionCallRequestFromAI);


        ChatbotRequest.Message toolMessageResponseToAI = ChatbotRequest.Message.builder()
                .role("tool")
                .content(contentToolCall)
                .tool_call_id(messageResponseFromAI.getTool_calls().get(0).getId())
                .tool_calls(null)
                .build();
        messages.add(toolMessageResponseToAI);

        //luu doan chat tool call vao db
        ChatRequest chatToolCallRequest = new ChatRequest();
        chatToolCallRequest.setContent(contentToolCall);
        chatToolCallRequest.setRole(RoleChat.tool);
        chatToolCallRequest.setCustomerId(req.getCustomerId());
        chatToolCallRequest.setToolCallId(messageResponseFromAI.getTool_calls().get(0).getId());
        log.info("TOOL CALL ID: {}", messageResponseFromAI.getTool_calls().get(0).getId());
        chatToolCallRequest.setToolCalls(null);
        chatHistoryService.saveAndFlush(chatToolCallRequest);

        //gửi dữ liệu đến AI
        return CallAIAPi(messages, req);
    }


}
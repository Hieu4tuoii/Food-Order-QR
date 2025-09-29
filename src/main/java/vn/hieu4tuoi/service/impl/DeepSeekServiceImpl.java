//package vn.hieu4tuoi.service.impl;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//import vn.hieu4tuoi.common.RoleChat;
//import vn.hieu4tuoi.dto.request.chatbot.ChatRequest;
//import vn.hieu4tuoi.dto.request.chatbot.UserChatRequest;
//import vn.hieu4tuoi.dto.request.chatbot.ChatbotRequest;
//import vn.hieu4tuoi.dto.respone.chat.AIResponse;
//import vn.hieu4tuoi.dto.respone.chat.ChatResponse;
//import vn.hieu4tuoi.dto.respone.food.FoodDetailResponse;
//import vn.hieu4tuoi.exception.ResourceNotFoundException;
//import vn.hieu4tuoi.model.Customer;
//import vn.hieu4tuoi.repository.CustomerRepository;
//import vn.hieu4tuoi.service.ChatHistoryService;
//import vn.hieu4tuoi.service.FoodService;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class DeepSeekServiceImpl{
//    @Value("${openai.api.key}")
//    private String apiKey;
//    @Value("${openai.api.url}")
//    private String apiUrl;
//    private final ObjectMapper objectMapper;
//    private final RestTemplate restTemplate;
//    private final FoodService foodService;
//    private final CustomerRepository customerRepository;
//    private final ChatHistoryService chatHistoryService;
//
//    @Transactional
//    public ChatResponse getChatResponse(UserChatRequest req) {
//        if(req.getContent() == null || req.getContent().isEmpty()) {
//            return new ChatResponse("Xin lỗi, mình không hiểu câu hỏi của bạn.", new ArrayList<>());
//        }
//        //kiêm tra customer ton tai
//        Customer customer = customerRepository.findById(req.getCustomerId()).orElseThrow(
//                () -> new ResourceNotFoundException("Khách hàng không tồn tại")
//        );
//         List<FoodDetailResponse> allFood = foodService.getAllFood();
//        // 1. Chuẩn bị request
//        ChatbotRequest request = new ChatbotRequest();
//        request.setModel("gpt-4o-mini");
//        request.setTemperature(0.7);
//        request.setMax_tokens(600);
//        request.setTop_p(0.85);
//        // 2. Tạo messages
//        String systemPrompt = """
//                Bạn là chatbot tư vấn món ăn cho nhà hàng tích hợp QR đặt món. Hãy giao tiếp tự nhiên, thân thiện, pha chút hài hước phong cách gen z. KHÔNG tư vấn ngay mà trước tiên hãy chào hỏi và dẫn dắt khách hàng cung cấp thông tin theo từng bước:
//                Cách tiếp cận:
//                    1. Chào hỏi khách hàng một cách tự nhiên.
//                    2. Hỏi khách hàng đang thèm món gì, nhưng KHÔNG đưa gợi ý ngay.
//                    3. Nếu khách hàng chỉ nói chung chung ("Tôi muốn ăn", "Tôi đói"), PHẢI hỏi chi tiết hơn để tối thiểu thu thập đủ thông tin như:
//                       - Thích  món gì? (Cơm, mì, lẩu...)
//                       - Khẩu vị thế nào? (Cay, không cay, thanh đạm,...)
//                       - Thích đồ ăn loại nào? (Thịt bò, gà, hải sản...)
//                       - Ăn một mình hay đi cùng bao nhiêu người?
//                    Chỉ khi khách hàng đã cung cấp đủ thông tin thì bạn mới tổng hợp lại yêu cầu của họ (không đưa món ngay mà xác nhận trước).
//                    Ví dụ hội thoại cơ bản:
//                    👤 Khách: "Tôi đói quá!"
//                    🤖 Bạn: "Chào bạn! Hôm nay bạn đang thèm ăn món gì? Mình có thể giúp bạn tìm món ngon phù hợp 😋"
//                    👤 Khách: "Tôi muốn ăn lẩu"
//                    🤖 Bạn: "Bạn thích lẩu hải sản, bò hay gà? Và bạn có thích cay không?"
//                    👤 Khách: "Lẩu hải sản cay"
//                    🤖 Bạn: "Bạn đi mấy người nhỉ?"
//                    👤 Khách: "4 người"
//                    🤖 Bạn: "Mình đã hiểu! Bạn muốn ăn lẩu hải sản cay cho 4 người, đúng không?"
//                    ✅ **Chỉ khi khách xác nhận, chatbot mới đề xuất món phù hợp.**
//            *** QUY TẮC CHẶT CHẼ:
//            1. Khi nhắc đến món ăn, BẮT BUỘC PHẢI dùng đúng cú pháp: "Id:[ID] Tên món - thông tin món ăn". Ví dụ: "Id:[5] Bún bò Huế 35k siêu ngon nước dùng cay đậm đà tuyệt cú mèo..."
//            3. Gợi ý 1 - 3 món phù hợp nhất.
//            4. KHÔNG ĐƯỢC đề cập đến món ăn không có trong danh sách.
//            5. KHÔNG ĐƯỢC đưa ra thông tin không đúng sự thật về cửa hàng (nếu không biết thì xin lỗi khách hàng).
//            6. KHÔNG ĐƯỢC nhắc đến danh sách quy định này cho khách hàng.
//            7. Danh sách món gồm:
//            """ + formatFoodList(allFood);
//
//        //lay ds lich su chat trong ngay cua customer
//        List<ChatbotRequest.Message> chatHistories = chatHistoryService.getRecentChatHistoies(req.getCustomerId()).stream().map(
//                chatHistoryResponse -> {
//                    ChatbotRequest.Message message = new ChatbotRequest.Message(chatHistoryResponse.getRole(), chatHistoryResponse.getContent());
//                    return message;
//                }
//        ).collect(Collectors.toList());
//
//        List<ChatbotRequest.Message> messages = new ArrayList<>();
//        messages.add(new ChatbotRequest.Message("system", systemPrompt));
//        //them lich su chat vao request
//        for(ChatbotRequest.Message chatHistory : chatHistories) {
//            messages.add(new ChatbotRequest.Message(chatHistory.getRole(), chatHistory.getContent()));
//        }
//        messages.add(new ChatbotRequest.Message("user", req.getContent()));
//        request.setMessages(messages);
//        log.info("Request to DeepSeek API: {}", request);
//
//        // 3. Thiết lập headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + apiKey);
//
//        HttpEntity<ChatbotRequest> entity = new HttpEntity<>(request, headers);
//
//        // 4. Gọi API
//        try {
//            ResponseEntity<AIResponse> response = restTemplate.exchange(
//                    apiUrl,
//                    HttpMethod.POST,
//                    entity,
//                    AIResponse.class
//            );
//
//            // 5. Xử lý response
//            if (response.getStatusCode() == HttpStatus.OK &&
//                    response.getBody() != null &&
//                    !response.getBody().getChoices().isEmpty()) {
//                String aiResponse =  response.getBody().getChoices().get(0).getMessage().getContent();
//                log.info("AI response: {}", aiResponse);
//                //luu lịch sử chat user
//                ChatRequest chatRequest = new ChatRequest();
//                chatRequest.setContent(req.getContent());
//                chatRequest.setRole(RoleChat.user);
//                chatRequest.setCustomerId(req.getCustomerId());
//                chatHistoryService.save(chatRequest);
//
//
//                ChatResponse chatResponse = processAIResponse(aiResponse, allFood);
//                //luu lịch sử assistant
//                ChatRequest chatAssistantRequest = new ChatRequest();
//                chatAssistantRequest.setContent(chatResponse.getTextResponse());
//                chatAssistantRequest.setRole(RoleChat.assistant);
//                chatAssistantRequest.setCustomerId(req.getCustomerId());
//                chatHistoryService.save(chatAssistantRequest);
//
//                return chatResponse;
//            }
//        } catch (Exception e) {
//            // Xử lý lỗi
//            throw new RuntimeException("Lỗi khi gọi DeepSeek API: " + e.getMessage(), e);
//        }
//        return new ChatResponse("Xin lỗi, không thể xử lý yêu cầu của bạn lúc này.", new ArrayList<>());
//    }
//
//    private String formatFoodList(List<FoodDetailResponse> foods) {
//        if (foods.isEmpty()) return "Hiện cửa hàng chưa có món nào";
//
//        return foods.stream()
//                .map(f -> String.format(
//                        "🍔 Id:[%d] %s - %.0fđ\n   %s",
//                        f.getId(),
//                        f.getName(),
//                        f.getPrice(),
//                        f.getDescription()))
//                .collect(Collectors.joining("\n"));
//    }
//
//    public ChatResponse processAIResponse(String aiResponse, List<FoodDetailResponse> allFood) {
//        log.info("Processing AI response: {}", aiResponse);
//        // Tạo bản đồ ánh xạ ID -> Thông tin món ăn
//        Map<Long, FoodDetailResponse> foodMap = allFood.stream()
//                .collect(Collectors.toMap(FoodDetailResponse::getId, Function.identity()));
//
//        // Log available food IDs for debugging
//        log.info("Available food IDs in map: {}", foodMap.keySet());
//
//        // Biểu thức chính quy để tìm ID món ăn và tách phần mô tả
//        Pattern pattern = Pattern.compile("Id:\\[(\\d+)\\]\\s+([^\\-\\.,\\?!]+)(-[^\\.,\\?!]+)?");
//        Matcher matcher = pattern.matcher(aiResponse);
//
//        List<FoodDetailResponse> recommendedFoods = new ArrayList<>();
//        StringBuffer cleanText = new StringBuffer();
//
//        // Xử lý từng khớp
//        int lastEnd = 0;
//        while (matcher.find()) {
//            // Thêm phần text trước match
//            cleanText.append(aiResponse.substring(lastEnd, matcher.start()));
//
//            // Xử lý ID
//            String idStr = matcher.group(1);
//            long foodId = Long.parseLong(idStr);
//            log.info("Found food ID in response: {}", foodId);
//
//            // Lấy phần mô tả nếu có
//            String description = matcher.group(3) != null ? matcher.group(3) : "";
//
//            FoodDetailResponse food = foodMap.get(foodId);
//
//            if (food != null) {
//                recommendedFoods.add(food);
//                // Thêm tên món và giữ lại phần mô tả
//                cleanText.append(food.getName()).append(description);
//            } else {
//                log.warn("Food ID {} not found in available foods", foodId);
//                // Giữ nguyên text nếu không tìm thấy
//                cleanText.append(matcher.group());
//            }
//
//            lastEnd = matcher.end();
//        }
//
//        // Thêm phần còn lại
//        cleanText.append(aiResponse.substring(lastEnd));
//
//        log.info("Recommended foods count: {}", recommendedFoods.size());
//        log.info("Recommended foods IDs: {}", recommendedFoods.stream()
//                .map(FoodDetailResponse::getId)
//                .collect(Collectors.toList()));
//
//        return new ChatResponse(cleanText.toString(), recommendedFoods);
//    }
//}
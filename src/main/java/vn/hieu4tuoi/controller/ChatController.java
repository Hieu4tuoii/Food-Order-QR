package vn.hieu4tuoi.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.hieu4tuoi.dto.request.chatbot.ChatToAiRequest;
import vn.hieu4tuoi.dto.request.chatbot.DeepSeekRequest;
import vn.hieu4tuoi.dto.respone.ChatHistoryResponse;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.ResponseData;
import vn.hieu4tuoi.dto.respone.chat.ChatResponse;
import vn.hieu4tuoi.service.ChatHistoryService;
import vn.hieu4tuoi.service.impl.DeepSeekServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final DeepSeekServiceImpl deepSeekService;
    private final ChatHistoryService chatHistoryService;

    @PostMapping("/")
    public ChatResponse handleChatMessage(@RequestBody ChatToAiRequest request) {
        try {
            return deepSeekService.getChatResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
            return new ChatResponse("Đã có lỗi xảy ra. Vui lòng thử lại sau.", new ArrayList<>());
        }
    }

    //lay ds chat history (phan trang lay 20 tin nhan gan nhat)
    @GetMapping("/history/{customerId}")
    public ResponseData<PageResponse<List<ChatHistoryResponse>>> getChatHistory(@PathVariable @Min(value = 1, message = "customerId must be equals or greater than 1") Long customerId,
                                                                                @RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "20") int size) {
        PageResponse<List<ChatHistoryResponse>> chatHistories = chatHistoryService.getChatHistoriesByCustomerId(customerId, page, size);
        return ResponseData.<PageResponse<List<ChatHistoryResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("Get chat history successfully")
                .data(chatHistories)
                .build();

    }
}

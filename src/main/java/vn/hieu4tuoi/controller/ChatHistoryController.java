package vn.hieu4tuoi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hieu4tuoi.dto.respone.chatHistory.ChatHistoryResponse;
import vn.hieu4tuoi.dto.request.chatHistory.ChatHistoryRequest;
import vn.hieu4tuoi.service.ChatHistoryService;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatHistoryController {
    
    private final ChatHistoryService chatHistoryService;
    
    /**
     * Create a new chat history entry
     * 
     * @param request Chat message details
     * @return The created chat history
     */
    @PostMapping("/")
    public ResponseEntity<ChatHistoryResponse> createChatHistory(@Valid @RequestBody ChatHistoryRequest request) {
        ChatHistoryResponse createdChatHistory = chatHistoryService.save(request);
        return new ResponseEntity<>(createdChatHistory, HttpStatus.CREATED);
    }
    
    /**
     * Get paginated chat histories for a specific customer
     * 
     * @param customerId ID of the customer
     * @return Page of chat histories
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<ChatHistoryResponse>> getChatHistoriesByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<ChatHistoryResponse> chatHistories = chatHistoryService.getChatHistoriesByCustomerId(customerId,page, size);
        return ResponseEntity.ok(chatHistories);
    }
}

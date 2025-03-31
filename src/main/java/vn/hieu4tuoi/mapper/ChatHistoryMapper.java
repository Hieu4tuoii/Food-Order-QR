package vn.hieu4tuoi.mapper;

import org.springframework.stereotype.Component;
import vn.hieu4tuoi.dto.request.chatbot.ChatRequest;
import vn.hieu4tuoi.dto.respone.ChatHistoryResponse;
import vn.hieu4tuoi.dto.request.chatbot.ChatToAiRequest;
import vn.hieu4tuoi.model.ChatHistory;
import vn.hieu4tuoi.model.Customer;

@Component
public class ChatHistoryMapper {
    
    public ChatHistoryResponse toDto(ChatHistory chatHistory) {
        return ChatHistoryResponse.builder()
                .id(chatHistory.getId())
                .message(chatHistory.getMessage())
                .role(chatHistory.getRole().toString())
                .createdAt(chatHistory.getCreatedAt())
                .build();
    }
    
    public ChatHistory toEntity(ChatRequest request, Customer customer) {
        return ChatHistory.builder()
                .message(request.getMessage())
                .customer(customer)
                .role(request.getRole())
                .build();
    }
}

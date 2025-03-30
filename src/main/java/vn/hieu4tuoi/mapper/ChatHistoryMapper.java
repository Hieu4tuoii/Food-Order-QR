package vn.hieu4tuoi.mapper;

import org.springframework.stereotype.Component;
import vn.hieu4tuoi.dto.respone.chatHistory.ChatHistoryResponse;
import vn.hieu4tuoi.dto.request.chatHistory.ChatHistoryRequest;
import vn.hieu4tuoi.model.ChatHistory;
import vn.hieu4tuoi.model.Customer;

@Component
public class ChatHistoryMapper {
    
    public ChatHistoryResponse toDto(ChatHistory chatHistory) {
        return ChatHistoryResponse.builder()
                .id(chatHistory.getId())
                .message(chatHistory.getMessage())
                .role(chatHistory.getRole())
                .createdAt(chatHistory.getCreatedAt())
                .build();
    }
    
    public ChatHistory toEntity(ChatHistoryRequest request, Customer customer) {
        return ChatHistory.builder()
                .message(request.getMessage())
                .customer(customer)
                .build();
    }
}

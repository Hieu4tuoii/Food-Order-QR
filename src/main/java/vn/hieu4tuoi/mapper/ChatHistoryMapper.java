package vn.hieu4tuoi.mapper;

import org.springframework.stereotype.Component;
import vn.hieu4tuoi.dto.request.chatbot.ChatRequest;
import vn.hieu4tuoi.dto.respone.chat.ChatHistoryResponse;
import vn.hieu4tuoi.model.ChatHistory;
import vn.hieu4tuoi.model.Customer;

@Component
public class ChatHistoryMapper {

    public ChatHistoryResponse toDto(ChatHistory chatHistory) {
        return ChatHistoryResponse.builder()
                .id(chatHistory.getId())
                .content(chatHistory.getContent())
                .role(chatHistory.getRole().toString())
                .toolCalls(chatHistory.getToolCalls())
                .toolCallId(chatHistory.getToolCallId())
                .createdAt(chatHistory.getCreatedAt())
                .build();
    }

    public ChatHistory toEntity(ChatRequest request, Customer customer) {
        return ChatHistory.builder()
                .content(request.getContent())
                .customer(customer)
                .role(request.getRole())
                .toolCalls(request.getToolCalls())
                .toolCallId(request.getToolCallId())
                .build();
    }
}

package vn.hieu4tuoi.service;

import org.springframework.data.domain.Page;
import vn.hieu4tuoi.dto.request.chatbot.ChatRequest;
import vn.hieu4tuoi.dto.respone.ChatHistoryResponse;
import vn.hieu4tuoi.dto.request.chatbot.ChatToAiRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;

import java.util.List;

public interface ChatHistoryService {
    ChatHistoryResponse save(ChatRequest request);
    PageResponse<List<ChatHistoryResponse>> getChatHistoriesByCustomerId(Long customerId, int page, int size);
    List<ChatHistoryResponse> getRecentChatHistoies(Long customerId);
}

package vn.hieu4tuoi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.hieu4tuoi.dto.respone.chatHistory.ChatHistoryResponse;
import vn.hieu4tuoi.dto.request.chatHistory.ChatHistoryRequest;

import java.util.List;

public interface ChatHistoryService {
    ChatHistoryResponse save(ChatHistoryRequest request);
    Page<ChatHistoryResponse> getChatHistoriesByCustomerId(Long customerId, int page, int size);
    List<ChatHistoryResponse> getRecentChatHistoies(Long customerId);
}

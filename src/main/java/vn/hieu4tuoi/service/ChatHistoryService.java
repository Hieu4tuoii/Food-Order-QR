package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.chatbot.ChatRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.chat.ChatHistoryResponse;

import java.util.List;

public interface ChatHistoryService {
    Long save(ChatRequest request);
    Long saveAndFlush(ChatRequest request);
    PageResponse<List<ChatHistoryResponse>> getChatHistoriesByCustomerId(Long customerId, int page, int size);
    List<ChatHistoryResponse> getRecentChatHistoies(Long customerId);
    //find by id
}

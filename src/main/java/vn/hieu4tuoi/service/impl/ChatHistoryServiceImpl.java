package vn.hieu4tuoi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hieu4tuoi.dto.request.chatbot.ChatRequest;
import vn.hieu4tuoi.dto.request.chatbot.ChatToAiRequest;
import vn.hieu4tuoi.dto.respone.ChatHistoryResponse;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.exception.ResourceNotFoundException;
import vn.hieu4tuoi.mapper.ChatHistoryMapper;
import vn.hieu4tuoi.model.ChatHistory;
import vn.hieu4tuoi.model.Customer;
import vn.hieu4tuoi.repository.ChatHistoryRepository;
import vn.hieu4tuoi.repository.CustomerRepository;
import vn.hieu4tuoi.service.ChatHistoryService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatHistoryServiceImpl implements ChatHistoryService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final CustomerRepository customerRepository;
    private final ChatHistoryMapper chatHistoryMapper;

    @Override
    @Transactional
    public ChatHistoryResponse save(ChatRequest request) {
        // Find the customer or throw an exception if not found
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        // Create and save the chat history
        ChatHistory chatHistory = chatHistoryMapper.toEntity(request, customer);
        ChatHistory savedChatHistory = chatHistoryRepository.save(chatHistory);

        // Return the DTO of the saved chat history
        return chatHistoryMapper.toDto(savedChatHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<List<ChatHistoryResponse>> getChatHistoriesByCustomerId(Long customerId, int page, int size) {
        // Check if customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }
        Pageable pageable = PageRequest.of(page, size);

        Page<ChatHistory> chatHistories = chatHistoryRepository.findByCustomerIdOrderByCreatedAtDescRoleAsc(customerId, pageable);

        //chuyen chat history sang dto
        List<ChatHistoryResponse> chatHistoryResponses = chatHistories.getContent().stream()
                .map(chatHistoryMapper::toDto)
                .toList();

        //dao nguoc danh sach chat history
        List<ChatHistoryResponse> chatHistoryReverseResponses = new ArrayList<>(chatHistories.getContent().stream()
                .map(chatHistoryMapper::toDto)
                .toList());
        Collections.reverse(chatHistoryReverseResponses);

        return PageResponse.<List<ChatHistoryResponse>>builder()
                .items(chatHistoryReverseResponses)
                .pageNo(chatHistories.getNumber())
                .pageSize(chatHistories.getSize())
                .build();
    }

    @Override
    public List<ChatHistoryResponse> getRecentChatHistoies(Long customerId) {
        // Check if customer exists
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }

        // Get recent chat histories
        List<ChatHistory> chatHistories = chatHistoryRepository.findTop10ByCustomerIdAndCreatedAtBetweenOrderByCreatedAtDescRoleAsc(customerId, LocalDateTime.now().minusDays(1), LocalDateTime.now());

        //dao nguoc danh sach chat history
        Collections.reverse(chatHistories);

        // Map the entities to DTOs
        return chatHistories.stream()
                .map(chatHistoryMapper::toDto)
                .toList();
    }

}

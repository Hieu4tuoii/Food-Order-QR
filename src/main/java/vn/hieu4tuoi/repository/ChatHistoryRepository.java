package vn.hieu4tuoi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hieu4tuoi.model.ChatHistory;

import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    // Find chat histories by customer ID with pagination
    Page<ChatHistory> findByCustomerId(Long customerId, Pageable pageable);

    //hàm lấy ra ds chat history từ 2 tiếng truước đến hiện tại(ko cần phân trang)
    List<ChatHistory> findByCustomerIdAndCreatedAtBetween(Long customerId, String startTime, String endTime);

}

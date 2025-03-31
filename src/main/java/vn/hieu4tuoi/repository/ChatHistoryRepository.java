package vn.hieu4tuoi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hieu4tuoi.model.ChatHistory;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    Page<ChatHistory> findByCustomerIdOrderByCreatedAtDescRoleAsc(Long customerId, Pageable pageable);

    List<ChatHistory> findTop10ByCustomerIdAndCreatedAtBetweenOrderByCreatedAtDescRoleAsc(Long customerId, LocalDateTime startTime, LocalDateTime endTime);
}

package vn.hieu4tuoi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import vn.hieu4tuoi.common.RoleChat;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tbl_chat_history")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistory {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
    @Enumerated(EnumType.STRING)
    private RoleChat role;
    //quan hệ n-1 với customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    //thời gian tạo
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime createdAt;

}

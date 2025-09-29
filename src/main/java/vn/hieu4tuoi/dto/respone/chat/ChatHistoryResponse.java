package vn.hieu4tuoi.dto.respone.chat;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import vn.hieu4tuoi.common.RoleChat;
import vn.hieu4tuoi.model.Customer;
import vn.hieu4tuoi.model.ToolCall;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatHistoryResponse {
    private Long id;
    private String content;
    private String role;
    private List<ToolCall> toolCalls;
    private String toolCallId;
    private LocalDateTime createdAt;
}

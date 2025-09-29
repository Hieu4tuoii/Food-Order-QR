package vn.hieu4tuoi.dto.request.chatbot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hieu4tuoi.common.RoleChat;
import vn.hieu4tuoi.model.ToolCall;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {
    @NotBlank(message = "Message cannot be blank")
    private String content;
    private RoleChat role;
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;
    private String toolCallId;
    private List<ToolCall> toolCalls;
}

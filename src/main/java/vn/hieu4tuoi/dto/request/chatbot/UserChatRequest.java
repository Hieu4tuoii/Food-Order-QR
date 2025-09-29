package vn.hieu4tuoi.dto.request.chatbot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserChatRequest {
    @NotBlank(message = "Message cannot be blank")
    private String content;
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;
    @NotNull(message = "dinningTableId cannot be null")
    private String diningTableId;
}

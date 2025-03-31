package vn.hieu4tuoi.dto.request.chatbot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatToAiRequest {
    @NotBlank(message = "Message cannot be blank")
    private String message;
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;
}

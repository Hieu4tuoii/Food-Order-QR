package vn.hieu4tuoi.dto.request.chatHistory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistoryRequest {
    @NotBlank(message = "Message cannot be blank")
    private String message;
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;
}

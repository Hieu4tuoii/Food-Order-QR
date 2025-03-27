package vn.hieu4tuoi.dto.request.invoice;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class InvoiceCreationRequest {
    @NotNull(message = "customerId must not be null")
    private Long customerId;
    @NotNull(message = "diningTableId must not be null")
    private Long diningTableId;

}

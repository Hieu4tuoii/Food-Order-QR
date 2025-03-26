package vn.hieu4tuoi.dto.request.invoice;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import vn.hieu4tuoi.model.PaymentMethod;
import vn.hieu4tuoi.model.PaymentStatus;

@Getter
@ToString
public class InvoiceCreationRequest {
    @NotNull(message = "customerId must not be null")
    private Long customerId;
    

}

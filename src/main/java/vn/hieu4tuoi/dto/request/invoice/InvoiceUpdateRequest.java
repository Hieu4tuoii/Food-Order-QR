package vn.hieu4tuoi.dto.request.invoice;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import vn.hieu4tuoi.model.PaymentMethod;
import vn.hieu4tuoi.model.PaymentStatus;

@Getter
@ToString
public class InvoiceUpdateRequest {
    @NotNull(message = "id must not be null")
    @Min(value = 1, message = "id must be equals or greater than 1")
    private Long id;
    
    @NotNull(message = "paymentStatus must not be null")
    private PaymentStatus paymentStatus;
    
    @NotNull(message = "paymentMethod must not be null")
    private PaymentMethod paymentMethod;
}

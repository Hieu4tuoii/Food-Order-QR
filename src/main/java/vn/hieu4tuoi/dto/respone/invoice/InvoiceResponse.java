package vn.hieu4tuoi.dto.respone.invoice;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.hieu4tuoi.model.PaymentMethod;
import vn.hieu4tuoi.model.PaymentStatus;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class InvoiceResponse {
    private Long id;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private Long customerId;
    private String customerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

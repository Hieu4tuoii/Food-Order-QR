package vn.hieu4tuoi.dto.respone.invoice;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.hieu4tuoi.common.PaymentMethod;
import vn.hieu4tuoi.common.PaymentStatus;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class InvoiceResponse {
    private Long id;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private Long customerId;
    private String customerName;
    private String dinningTableName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package vn.hieu4tuoi.dto.respone.invoice;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.hieu4tuoi.common.PaymentMethod;
import vn.hieu4tuoi.common.PaymentStatus;
import vn.hieu4tuoi.dto.respone.customer.CustomerResponse;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@SuperBuilder
public class InvoiceResponse {
    private Long id;
    List<InvoiceItemResponse> items;
    private String dinningTableName;
    private CustomerResponse customer;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

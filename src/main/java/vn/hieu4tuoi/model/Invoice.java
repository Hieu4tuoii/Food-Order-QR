package vn.hieu4tuoi.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tbl_invoice")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invoice extends AbstractEntity<Long> {
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}

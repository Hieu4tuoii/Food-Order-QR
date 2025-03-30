package vn.hieu4tuoi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vn.hieu4tuoi.common.PaymentMethod;
import vn.hieu4tuoi.common.PaymentStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tbl_invoice")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @Id
    @Column(name = "id")
    private String id;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentMethod paymentMethod = PaymentMethod.CASH;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Order> orderList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dining_table_id")
    private DiningTable diningTable;
    
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<Order> orders = new ArrayList<>();

    @Column(name = "created_at", length = 255)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", length = 255)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    public void generateRandomId() {
        if (this.id == null) {
            //lay milis giay hien tai
            long currentTimeMillis = System.currentTimeMillis();
            String currentTimeString = String.valueOf(currentTimeMillis);
            String last10Digits = currentTimeString.substring(currentTimeString.length() - 10);
            this.id = (1000 + new java.util.Random().nextInt(9000) + last10Digits);
        }
    }

    //ham helper de them order vao invoice
    public void addOrder(Order order){
        orders.add(order);
        order.setInvoice(this);
    }
}

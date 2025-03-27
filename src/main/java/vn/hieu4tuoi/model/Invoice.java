package vn.hieu4tuoi.model;

import jakarta.persistence.*;
import lombok.*;
import vn.hieu4tuoi.common.PaymentMethod;
import vn.hieu4tuoi.common.PaymentStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_invoice")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invoice extends AbstractEntity<Long> {
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

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

    //ham helper de them order vao invoice
    public void addOrder(Order order){
        orders.add(order);
        order.setInvoice(this);
    }
}

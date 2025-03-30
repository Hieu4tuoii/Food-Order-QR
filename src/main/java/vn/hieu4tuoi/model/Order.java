package vn.hieu4tuoi.model;
import jakarta.persistence.*;
import lombok.*;
import vn.hieu4tuoi.common.OrderStatus;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_order")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order extends AbstractEntity<Long> {
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING; //mac dinh la dang cho
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    //ham helper de them order detail vao order
    public void addOrderDetail(OrderDetail orderDetail){
        orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }
}

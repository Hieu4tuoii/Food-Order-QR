package vn.hieu4tuoi.model;

import jakarta.persistence.*;
import lombok.*;
import vn.hieu4tuoi.common.OrderStatus;

@Getter
@Setter
@Entity
@Table(name = "tbl_order_detail")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail extends AbstractEntity<Long> {
    
    private Integer quantity;
    
    private Double priceAtOrder;

    private OrderStatus status = OrderStatus.PENDING; // mac dinh la dang cho
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;
}

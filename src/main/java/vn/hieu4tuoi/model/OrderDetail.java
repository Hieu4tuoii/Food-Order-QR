package vn.hieu4tuoi.model;

import jakarta.persistence.*;
import lombok.*;

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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;
}

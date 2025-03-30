package vn.hieu4tuoi.dto.respone.order;

import lombok.*;
import vn.hieu4tuoi.common.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private OrderStatus status;

    private List<OrderDetailResponse> orderDetail;
    private Double totalPrice;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;
}
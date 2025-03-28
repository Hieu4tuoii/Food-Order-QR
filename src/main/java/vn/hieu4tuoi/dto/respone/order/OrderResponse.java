package vn.hieu4tuoi.dto.respone.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.hieu4tuoi.common.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private OrderStatus status;

    private List<OrderDetailResponse> orderDetail;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;
}
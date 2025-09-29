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

    public String toJson() {
        // Convert orderDetail list to a JSON array string
        String orderDetailJson = orderDetail.stream()
                .map(OrderDetailResponse::toJson)
                .collect(java.util.stream.Collectors.joining(",", "[", "]"));

        return String.format("{\"id\":%d,\"status\":\"%s\",\"orderDetail\":%s,\"totalPrice\":%.2f}",
                id, status, orderDetailJson, totalPrice);
    }
}
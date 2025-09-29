package vn.hieu4tuoi.dto.request.order;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class OrderItemByAIRequest {
    private Long food_id;
    private Integer quantity;
}

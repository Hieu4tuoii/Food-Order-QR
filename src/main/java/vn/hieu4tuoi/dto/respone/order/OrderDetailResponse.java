package vn.hieu4tuoi.dto.respone.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import vn.hieu4tuoi.common.OrderStatus;
import vn.hieu4tuoi.dto.respone.food.BaseFoodResponse;
import vn.hieu4tuoi.dto.respone.food.FoodResponse;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private Long id;
    private Integer quantity;
    private FoodResponse food;
    private OrderStatus status;
    private Double totalPrice;


}
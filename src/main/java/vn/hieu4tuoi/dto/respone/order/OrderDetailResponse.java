package vn.hieu4tuoi.dto.respone.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.hieu4tuoi.common.OrderStatus;
import vn.hieu4tuoi.dto.respone.food.BaseFoodResponse;
import vn.hieu4tuoi.dto.respone.food.FoodResponse;

@Data
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
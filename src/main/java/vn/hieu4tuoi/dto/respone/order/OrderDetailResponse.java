package vn.hieu4tuoi.dto.respone.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.hieu4tuoi.dto.respone.food.FoodResponse;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {

    private Long id;

    private Double priceAtOrder;

    private Integer quantity;

    private FoodResponse food;
}
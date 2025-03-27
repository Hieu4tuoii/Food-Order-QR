package vn.hieu4tuoi.dto.respone.cartDetail;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.hieu4tuoi.dto.respone.customer.CustomerResponse;
import vn.hieu4tuoi.dto.respone.food.FoodDetailResponse;
import vn.hieu4tuoi.dto.respone.food.FoodResponse;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class CartDetailResponse {
    private Long id;
    private Integer quantity;
    private FoodResponse food;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

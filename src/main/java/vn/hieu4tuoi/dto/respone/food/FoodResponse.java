package vn.hieu4tuoi.dto.respone.food;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder()
public class FoodResponse extends BaseFoodResponse {
    private Double price;
}

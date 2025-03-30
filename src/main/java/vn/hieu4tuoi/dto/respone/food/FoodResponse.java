package vn.hieu4tuoi.dto.respone.food;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.hieu4tuoi.common.FoodStatus;

@Getter
@SuperBuilder()
public class FoodResponse extends BaseFoodResponse {
    private Double price;
    private FoodStatus status;
}

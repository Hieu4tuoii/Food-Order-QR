package vn.hieu4tuoi.dto.respone.food;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder()
public class FoodResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private Long price;
}

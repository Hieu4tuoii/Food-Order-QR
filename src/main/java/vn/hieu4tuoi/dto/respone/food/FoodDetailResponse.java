package vn.hieu4tuoi.dto.respone.food;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder()
public class FoodDetailResponse extends FoodResponse {
    private String description;

    //hàm format thành json object
    public String toJson() {
        return String.format("{\"id\":%d,\"name\":\"%s\",\"description\":\"%s\",\"imageUrl\":\"%s\"}",
                id, name, description, imageUrl);
    }
}


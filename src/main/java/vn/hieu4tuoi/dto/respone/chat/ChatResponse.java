package vn.hieu4tuoi.dto.respone.chat;

import lombok.*;
import vn.hieu4tuoi.dto.respone.food.FoodDetailResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatResponse {
    private String textResponse;
    private List<FoodDetailResponse> recommendedFoods;
}

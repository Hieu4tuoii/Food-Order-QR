package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.food.FoodCreationRequest;
import vn.hieu4tuoi.dto.respone.food.FoodDetailResponse;
import vn.hieu4tuoi.dto.respone.PageResponse;

public interface FoodService {
    PageResponse getFoodList(String keyword, String sort, int page, int size);
    PageResponse getFoodListByCategoryId(String categoryId, String keyword, String sort, int page, int size);
    FoodDetailResponse getById(Long foodId);
    Long save(FoodCreationRequest req);
}

package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.food.FoodCreationRequest;
import vn.hieu4tuoi.dto.request.food.FoodUpdateRequest;
import vn.hieu4tuoi.dto.respone.food.FoodDetailResponse;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.food.FoodResponse;

import java.util.List;

public interface FoodService {
    PageResponse<List<FoodResponse>> getFoodList(String keyword, String sort, int page, int size);
    PageResponse<List<FoodResponse>> getFoodListByCategoryId(String categoryId, String keyword, String sort, int page, int size);
    FoodDetailResponse getById(Long foodId);
    Long save(FoodCreationRequest req);
    void update(FoodUpdateRequest request);
    void delete(Long foodId);
}

package vn.hieu4tuoi.service.impl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.hieu4tuoi.dto.request.food.FoodCreationRequest;
import vn.hieu4tuoi.dto.request.food.FoodUpdateRequest;
import vn.hieu4tuoi.dto.respone.food.FoodDetailResponse;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.food.FoodResponse;
import vn.hieu4tuoi.exception.ResourceNotFoundException;
import vn.hieu4tuoi.model.Category;
import vn.hieu4tuoi.model.Food;
import vn.hieu4tuoi.repository.CategoryRepository;
import vn.hieu4tuoi.repository.FoodRepository;
import vn.hieu4tuoi.service.FoodService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "FOOD_SERVICE")
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public PageResponse getFoodList(String keyword, String sort, int page, int size) {
        log.info("Getting comment by keyword {} sort: {}, page: {}, size: {}", keyword, sort, page, size);
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createdAt");//mac dinh sap xep theo thoi gian cap nhat giam dan
        if(StringUtils.hasLength(sort)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()){
                String columnName = matcher.group(1);
                order = matcher.group(3).equalsIgnoreCase("asc")
                        ? new Sort.Order(Sort.Direction.ASC, columnName)
                        : new Sort.Order(Sort.Direction.DESC, columnName);
            }
        }

        //xu ly page = 0
        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<Food> foodPage;
        if(StringUtils.hasLength(keyword)){
            keyword = "%" + keyword.toLowerCase() + "%";
            foodPage = foodRepository.searchByKeyword(keyword, pageable);
        }else{
            foodPage = foodRepository.findAll(pageable);
        }
        List<Food> foodList = foodPage.getContent();
        //map sang foodResponse
        List<FoodResponse> foodResponseList = foodList.stream()
                .map(food -> FoodResponse.builder()
                        .id(food.getId())
                        .name(food.getName())
                        .price(food.getPrice())
                        .imageUrl(food.getImageUrl())
                        .build())
                .collect(java.util.stream.Collectors.toList());

        log.info("Got food by keyword {} sort: {}, page: {}, size: {}", keyword, sort, page, size);
        return PageResponse.builder()
                .pageNo(page + 1)
                .pageSize(size)
                .totalPage(foodPage.getTotalPages())
                .items(foodResponseList)
                .build();
    }

    @Override
    public PageResponse getFoodListByCategoryId(String categoryId, String keyword, String sort, int page, int size) {
        log.info("Getting food by categoryId {} keyword {} sort: {}, page: {}, size: {}", categoryId, keyword, sort, page, size);
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createdAt");//mac dinh sap xep theo thoi gian cap nhat giam dan
        if (StringUtils.hasLength(sort)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                order = matcher.group(3).equalsIgnoreCase("asc")
                        ? new Sort.Order(Sort.Direction.ASC, columnName)
                        : new Sort.Order(Sort.Direction.DESC, columnName);
            }
        }

        //xu ly page = 0
        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<Food> foodPage;
        if (StringUtils.hasLength(keyword)) {
            foodPage = foodRepository.searchByKeywordAndCategoryId(Long.valueOf(categoryId), keyword, pageable);
        } else {
            foodPage = foodRepository.searchByCategoryId(Long.valueOf(categoryId), pageable);
        }

        List<Food> foodList = foodPage.getContent();
        //map sang foodResponse
        List<FoodResponse> foodResponseList = foodList.stream()
                .map(food -> FoodResponse.builder()
                        .id(food.getId())
                        .name(food.getName())
                        .price(food.getPrice())
                        .imageUrl(food.getImageUrl())
                        .build())
                .collect(java.util.stream.Collectors.toList());

        log.info("Got food by categoryId {} keyword {} sort: {}, page: {}, size: {}", categoryId,  keyword, sort, page, size);
        return PageResponse.builder()
                .pageNo(page + 1)
                .pageSize(size)
                .totalPage(foodPage.getTotalPages())
                .items(foodResponseList)
                .build();
    }

    @Override
    public FoodDetailResponse getById(Long foodId) {
        log.info("Getting food by id {}", foodId);
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food not found"));
        return FoodDetailResponse.builder()
                .id(food.getId())
                .name(food.getName())
                .description(food.getDescription())
                .price(food.getPrice())
                .imageUrl(food.getImageUrl())
                .build();
    }

    @Override
    public Long save(FoodCreationRequest req) {
        //tim category
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        //tao food
        Food food = Food.builder()
                .name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .category(category)
                .imageUrl(req.getImageUrl())
                .build();
        foodRepository.save(food);
        return food.getId();
    }

    @Override
    public void update(FoodUpdateRequest request) {
        log.info("Updating food with id: {}", request.getId());
        Food food = foodRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Food not found"));
        
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        food.setName(request.getName());
        food.setDescription(request.getDescription());
        food.setPrice(request.getPrice());
        food.setImageUrl(request.getImageUrl());
        food.setCategory(category);
        
        foodRepository.save(food);
        log.info("Food updated successfully: {}", food.getId());
    }

    @Override
    public void delete(Long foodId) {
        log.info("Deleting food with id: {}", foodId);
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found"));
        
        foodRepository.delete(food);
        log.info("Food deleted successfully: {}", foodId);
    }
}

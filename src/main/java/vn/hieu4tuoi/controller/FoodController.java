package vn.hieu4tuoi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hieu4tuoi.dto.request.food.FoodCreationRequest;
import vn.hieu4tuoi.dto.request.food.FoodStatusChangeRequest;
import vn.hieu4tuoi.dto.request.food.FoodUpdateRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.food.FoodDetailResponse;
import vn.hieu4tuoi.dto.respone.ResponseData;
import vn.hieu4tuoi.dto.respone.food.FoodResponse;
import vn.hieu4tuoi.service.FoodService;

import java.util.List;

@RestController
@RequestMapping("/food")
@Tag(name = "Food Controller")
@Slf4j(topic = "FOOD-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class FoodController {
    private final FoodService foodService;
    @Operation(summary = "Find food detail by id, " +
            "foodStatus gồm: AVAILABLE, UNAVAILABLE")
    @GetMapping("/{id}")
    public ResponseData<FoodDetailResponse> getFoodDetail(@PathVariable @Min(value = 1, message = "userId must be equals or gretter than 1") Long id) {
        log.info("Getting food detail by id: {}", id);
        FoodDetailResponse foodDetailResponse = foodService.getById(id);
        return ResponseData.<FoodDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get food detail successfully")
                .data(foodDetailResponse)
                .build();
    }

    //hàm save food
    @Operation(summary = "Save food")
    @PostMapping("/")
    public ResponseData<Long> saveFood(@Valid @RequestBody FoodCreationRequest request) {
        log.info("Request save food {}", request.toString());
        long foodId = foodService.save(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Save food success", foodId);
    }

    //get ds food
    @Operation(summary = "get food list by keyword, sort (vd: price:asc), page, size",
            description = "keyword: tu khoa tim kiem (ko bắt buoc), sort: sap xep theo cot nao va chieu tang dan hoac giam dan(ko bat buoc), page (mac dinh trang 1), size: (mac dinh 10" +
                    "\"foodStatus gồm: AVAILABLE, UNAVAILABLE"
    )
    @GetMapping("/")
    public ResponseData<PageResponse<List<FoodResponse>>> getFoodList(@RequestParam(value = "keyword", required = false) String keyword,
                                                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                                                      @RequestParam(value = "size", defaultValue = "30") int size,
                                                                      @RequestParam(value = "sort", required = false) String sort) {
        log.info("Getting food list by keyword {} sort: {}, page: {}, size: {}", keyword, sort, page, size);
            return new ResponseData<>(HttpStatus.OK.value(), "get food list success", foodService.getFoodList( keyword, sort, page, size));
    }

    //find by category id
    @Operation(summary = "get food list by category id, keyword, sort (vd: price:asc), page, size",
            description = "keyword: tu khoa tim kiem (ko bắt buoc), sort: sap xep theo cot nao va chieu tang dan hoac giam dan(ko bat buoc), page (mac dinh trang 1), size: (mac dinh 10 " +
                    "\nfoodStatus gồm: AVAILABLE, UNAVAILABLE"
    )
    @GetMapping("/category/{categoryId}")
    public ResponseData<PageResponse<List<FoodResponse>>> getFoodListByCategoryId(@PathVariable String categoryId,
                                                     @RequestParam(value = "keyword", required = false) String keyword,
                                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                                     @RequestParam(value = "size", defaultValue = "10") int size,
                                                     @RequestParam(value = "sort", required = false) String sort) {
        log.info("Getting food list by category id {} keyword {} sort: {}, page: {}, size: {}", categoryId, keyword, sort, page, size);
            return new ResponseData<>(HttpStatus.OK.value(), "get food list success", foodService.getFoodListByCategoryId(categoryId, keyword, sort, page, size));
    }
    
    //update food
    @Operation(summary = "Update food")
    @PutMapping("/")
    public ResponseData<?> updateFood(@Valid @RequestBody FoodUpdateRequest request) {
        log.info("Request update food {}", request.toString());
        foodService.update(request);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Update food success", null);
    }
    
   // delete food
    @Operation(summary = "Delete food by id")
    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteFood(@PathVariable @Min(value = 1, message = "foodId must be equals or greater than 1") Long id) {
        log.info("Request delete food with id: {}", id);
        foodService.delete(id);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete food success", null);
    }

    //change status food
    @Operation(summary = "Change food status" ,description = "cập nhật trạng thái món ăn (AVAILABLE, UNAVAILABLE)")
    @PatchMapping("/changeStatus")
    public ResponseData<Void> changeFoodStatus(@Valid @RequestBody FoodStatusChangeRequest request) {
        log.info("Request change food {}", request.toString());
        foodService.changeStatus(request);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Change food status success", null);
    }
}

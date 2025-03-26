package vn.hieu4tuoi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hieu4tuoi.dto.request.category.CategoryCreationRequest;
import vn.hieu4tuoi.dto.respone.ResponseData;
import vn.hieu4tuoi.service.CategoryService;

@RestController
@RequestMapping("/category")
@Tag(name = "Category Controller")
@Slf4j(topic = "CATEGORY-SERVICE")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    @Operation(method = "POST", summary = "Add new user", description = "Send a request via this API to create new user")
    @PostMapping("/")
    public ResponseData<Long> saveCategory(@Valid @RequestBody CategoryCreationRequest request) {
        log.info("Request save category {}", request.toString());
        long categoryId = categoryService.save(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Save category success", categoryId);
    }
}

package vn.hieu4tuoi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.hieu4tuoi.dto.request.category.CategoryCreationRequest;
import vn.hieu4tuoi.dto.respone.category.CategoryResponse;
import vn.hieu4tuoi.model.Category;
import vn.hieu4tuoi.repository.CategoryRepository;
import vn.hieu4tuoi.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "CATEGORY-SERVICE")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public Long save(CategoryCreationRequest req) {
        Category category = Category.builder()
                .name(req.getName())
                .description(req.getDescription())
                .build();
        categoryRepository.save(category);
        return category.getId();
    }

    @Override
    public List<CategoryResponse> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}

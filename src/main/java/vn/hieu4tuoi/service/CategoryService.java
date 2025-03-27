package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.category.CategoryCreationRequest;
import vn.hieu4tuoi.dto.respone.category.CategoryResponse;

import java.util.List;

public interface CategoryService {
    Long save(CategoryCreationRequest req);
    List<CategoryResponse> getAll();
}

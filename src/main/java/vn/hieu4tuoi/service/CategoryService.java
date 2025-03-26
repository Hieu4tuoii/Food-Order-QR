package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.category.CategoryCreationRequest;

public interface CategoryService {
    Long save(CategoryCreationRequest req);
}

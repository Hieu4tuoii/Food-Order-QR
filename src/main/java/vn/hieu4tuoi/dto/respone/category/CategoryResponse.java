package vn.hieu4tuoi.dto.respone.category;

import lombok.Builder;

@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
}

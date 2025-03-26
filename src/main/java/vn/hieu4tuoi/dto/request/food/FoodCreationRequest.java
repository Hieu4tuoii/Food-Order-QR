package vn.hieu4tuoi.dto.request.food;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.ToString;
import vn.hieu4tuoi.model.Category;

@Getter
@ToString
public class FoodCreationRequest {
    private String name;
    private String imageUrl;
    private Long price;
    private String description;
    private Long categoryId;
}

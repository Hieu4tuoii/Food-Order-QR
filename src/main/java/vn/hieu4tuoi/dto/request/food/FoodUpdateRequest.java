package vn.hieu4tuoi.dto.request.food;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FoodUpdateRequest {
    @NotNull(message = "id must not be null")
    @Min(value = 1, message = "id must be equals or greater than 1")
    private Long id;
    
    @NotBlank(message = "name must not be blank")
    private String name;
    
    private String description;
    
    @NotNull(message = "price must not be null")
    @Min(value = 0, message = "price must be equals or greater than 0")
    private Long price;
    
    private String imageUrl;
    
    @NotNull(message = "categoryId must not be null")
    @Min(value = 1, message = "categoryId must be equals or greater than 1")
    private Long categoryId;
}

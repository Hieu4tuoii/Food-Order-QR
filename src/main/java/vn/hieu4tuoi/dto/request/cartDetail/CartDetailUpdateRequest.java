package vn.hieu4tuoi.dto.request.cartDetail;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CartDetailUpdateRequest {
    @NotNull(message = "id must not be null")
    @Min(value = 1, message = "id must be equals or greater than 1")
    private Long id;
    
    @NotNull(message = "quantity must not be null")
    @Min(value = 0, message = "quantity must be equals or greater than 0")
    private Integer quantity;
}

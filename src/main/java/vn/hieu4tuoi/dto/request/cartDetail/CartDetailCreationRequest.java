package vn.hieu4tuoi.dto.request.cartDetail;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CartDetailCreationRequest {
    @NotNull(message = "customerId must not be null")
    private Long customerId;
    
    @NotNull(message = "foodId must not be null")
    private Long foodId;
    
    @NotNull(message = "quantity must not be null")
    @Min(value = 1, message = "quantity must be equals or greater than 1")
    private Integer quantity;
}

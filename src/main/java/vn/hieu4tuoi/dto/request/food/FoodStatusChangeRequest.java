package vn.hieu4tuoi.dto.request.food;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import vn.hieu4tuoi.common.FoodStatus;

import java.io.Serializable;

@Getter
public class FoodStatusChangeRequest implements Serializable {
    @Min( value = 1, message = "id must be equals or greater than 1")
    private Long id;
    @NotNull( message = "status must not be null")
    private FoodStatus status;
}

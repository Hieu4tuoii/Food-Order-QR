package vn.hieu4tuoi.dto.request.customer;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomerUpdateRequest {
    @NotNull(message = "id must not be null")
    @Min(value = 1, message = "id must be equals or greater than 1")
    private Long id;
    
    @NotBlank(message = "name must not be blank")
    private String name;
}

package vn.hieu4tuoi.dto.request.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomerCreationRequest {
    @NotBlank(message = "name must not be blank")
    private String name;
}

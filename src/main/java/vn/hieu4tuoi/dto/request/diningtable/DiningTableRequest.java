package vn.hieu4tuoi.dto.request.diningtable;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DiningTableRequest {
    @NotBlank(message = "Table name cannot be blank")
    private String name;
}

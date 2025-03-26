package vn.hieu4tuoi.dto.request.category;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class CategoryCreationRequest implements Serializable {
    private String name;
    private String description;
}

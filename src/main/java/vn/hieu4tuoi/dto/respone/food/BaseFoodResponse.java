package vn.hieu4tuoi.dto.respone.food;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
@Getter
@SuperBuilder()
public class BaseFoodResponse {
    protected Long id;
    protected String name;
    protected String imageUrl;
}

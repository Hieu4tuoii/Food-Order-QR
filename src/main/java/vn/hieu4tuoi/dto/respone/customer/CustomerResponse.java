package vn.hieu4tuoi.dto.respone.customer;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CustomerResponse {
    private Long id;
    private String name;
}

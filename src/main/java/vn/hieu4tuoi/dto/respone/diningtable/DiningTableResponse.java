package vn.hieu4tuoi.dto.respone.diningtable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DiningTableResponse {
    private Long id;
    private String name;
}

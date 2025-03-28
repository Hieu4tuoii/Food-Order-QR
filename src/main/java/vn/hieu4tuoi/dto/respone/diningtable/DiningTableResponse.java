package vn.hieu4tuoi.dto.respone.diningtable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.hieu4tuoi.common.TableStatus;

@Getter
@Setter
@Builder
public class DiningTableResponse {
    private Long id;
    private String name;
    private TableStatus status;
}

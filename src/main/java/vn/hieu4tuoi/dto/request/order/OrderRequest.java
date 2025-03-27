package vn.hieu4tuoi.dto.request.order;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class OrderRequest implements Serializable {
    private Long customerId;
    private Long diningTableId;

}

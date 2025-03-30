package vn.hieu4tuoi.dto.request.order;

import lombok.Getter;
import vn.hieu4tuoi.common.OrderStatus;

import java.io.Serializable;

@Getter
public class OrderChangeStatusRequest implements Serializable {
    private Long id;
    private OrderStatus status;
}

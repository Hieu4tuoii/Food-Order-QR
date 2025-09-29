package vn.hieu4tuoi.dto.request.order;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class OrderByAIRequest {
    private Long customerId;
    private String diningTableId;
    private List<OrderItemByAIRequest> items;
}

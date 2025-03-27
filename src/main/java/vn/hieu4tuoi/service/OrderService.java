package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.order.OrderRequest;
import vn.hieu4tuoi.dto.respone.order.OrderResponse;

import java.util.List;

public interface OrderService {
    Long saveOrder(OrderRequest request) ;
    public List<OrderResponse> getOrderByDiningTableId(Long diningTableId);
}

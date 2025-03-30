package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.order.OrderChangeStatusRequest;
import vn.hieu4tuoi.dto.request.order.OrderRequest;
import vn.hieu4tuoi.dto.respone.order.OrderResponse;

import java.util.List;

public interface OrderService {
    Long saveOrder(OrderRequest request) ;
    List<OrderResponse> getOrderByDiningTableId(Long diningTableId);
    void changeOrderDetailStatus(OrderChangeStatusRequest request);
    void changeOrderStatus(OrderChangeStatusRequest request);
}

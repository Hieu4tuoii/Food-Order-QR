package vn.hieu4tuoi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hieu4tuoi.dto.request.order.OrderChangeStatusRequest;
import vn.hieu4tuoi.dto.request.order.OrderRequest;
import vn.hieu4tuoi.dto.respone.ResponseData;
import vn.hieu4tuoi.dto.respone.order.OrderResponse;
import vn.hieu4tuoi.service.OrderService;
import java.util.List;

@RestController
@RequestMapping("/order")
@Tag(name = "Order Controller")
@Slf4j(topic = "ORDER-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;
    @Operation(summary = "Đặt hàng cho 1 bàn ăn", description = "Đặt hàng cho 1 bàn ăn (tự lấy ds món trong cart của khách hàng)")
    @PostMapping("/")
    public ResponseData<Long> saveOrder(@Valid @RequestBody OrderRequest request) {
        log.info("Request save order {}", request.toString());
        Long orderId = orderService.saveOrder(request);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "order success", orderId);
    }

    @Operation(summary = "Lấy order list của bàn ăn", description = "Lấy ds đơn hàng thuộc hóa đơn đang được đặt tại bàn ăn")
    //get order by dining table id ( lay nhung order cua hoa don dang duoc dat)
     @GetMapping("/diningTable/{diningTableId}")
    public ResponseData<List<OrderResponse>> getOrderByDiningTableId(@PathVariable String diningTableId) {
         log.info("Request get order by dining table id {}", diningTableId);
         return new ResponseData<>(HttpStatus.OK.value(), "get order success", orderService.getOrderByDiningTableId(diningTableId));
    }

    @Operation(summary = "Thay đổi trạng thái orderDetail(admin)", description = "1 Đơn hàng có nhiều món ăn, moi món ăn tương tương 1 orderDetail, khi nhân viên cbi xong món nào thì cập nhat trạng thái cho orderDetail đó" +
            "orderStatus gồm PENDING, DELIVERED, CANCELLED")
    @PatchMapping("/orderDetail/changeStatus")
    public ResponseData<Void> changeOrderDetailStatus(@Valid @RequestBody OrderChangeStatusRequest request) {
        log.info("Request change order detail status {}", request.toString());
        orderService.changeOrderDetailStatus(request);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "change order detail status success", null);
    }

    //Thay đổi trạng thái order
    @Operation(summary = "Thay đổi trạng thái order(admin)", description = "Thay đổi trạng thái của cả đơn hàng," +
            "orderStatus gồm PENDING, DELIVERED, CANCELLED")
    @PatchMapping("/changeStatus")
    public ResponseData<Void> changeOrderStatus(@Valid @RequestBody OrderChangeStatusRequest request) {
        log.info("Request change order status {}", request.toString());
        orderService.changeOrderStatus(request);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "change order status success", null);
    }
}

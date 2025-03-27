package vn.hieu4tuoi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hieu4tuoi.dto.request.invoice.InvoiceCreationRequest;
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
    @PostMapping("/")
    public ResponseData<Long> saveOrder(@Valid @RequestBody OrderRequest request) {
        log.info("Request save order {}", request.toString());
        Long orderId = orderService.saveOrder(request);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "order success", orderId);
    }

    //get order by dining table id ( lay nhung order cua hoa don dang duoc dat)
     @GetMapping("/diningTable/{diningTableId}")
    public ResponseData<List<OrderResponse>> getOrderByDiningTableId(@PathVariable Long diningTableId) {
         log.info("Request get order by dining table id {}", diningTableId);
         return new ResponseData<>(HttpStatus.OK.value(), "get order success", orderService.getOrderByDiningTableId(diningTableId));
     }
}

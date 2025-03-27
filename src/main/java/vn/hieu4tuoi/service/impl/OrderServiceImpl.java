package vn.hieu4tuoi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hieu4tuoi.common.PaymentStatus;
import vn.hieu4tuoi.dto.request.order.OrderRequest;
import vn.hieu4tuoi.dto.respone.food.FoodResponse;
import vn.hieu4tuoi.dto.respone.order.OrderDetailResponse;
import vn.hieu4tuoi.dto.respone.order.OrderResponse;
import vn.hieu4tuoi.exception.ResourceNotFoundException;
import vn.hieu4tuoi.model.*;
import vn.hieu4tuoi.repository.*;
import vn.hieu4tuoi.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final CustomerRepository customerRepository;
    private final DiningTableRepository diningTableRepository;
    private final InvoiceRepository invoiceRepository;

    //transaction neu co loi xay ra
    @Transactional
    @Override
    public Long saveOrder(OrderRequest request) {
        log.info("Request save order {}", request.toString());
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        DiningTable diningTable = diningTableRepository.findById(request.getDiningTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Dining table not found"));

        //lay hoa don gan nhat cua ban an
        Invoice invoice = invoiceRepository.findFirstByDiningTableIdOrderByCreatedAtDesc(diningTable.getId());

        Order order = new Order();

        //chuyen ds cartdetail sang orderdetail
        customer.getCartDetails().forEach( cartDetail -> {
            OrderDetail orderDetail = cartDetailToOrderDetail(cartDetail);
            order.addOrderDetail(orderDetail);
        });

        //neu hoa don gan nhat da thanh toan hoac null thi tao moi
        if(invoice == null || invoice.getPaymentStatus() == PaymentStatus.PAID){
            invoice = new Invoice();
            invoice.setDiningTable(diningTable);
            invoice.addOrder(order);
            invoice.setCustomer( customer);
        }else{
            //nguoc lai thi them order vao hoa don
            invoice.addOrder(order);
        }

        //save
        invoiceRepository.save(invoice);
        customer.getCartDetails().clear();
        customerRepository.save(customer);
        log.info(" Save order successfully with id: {}", order.getId());
        return order.getId();
    }

    //get order list by dining table id
    public List<OrderResponse> getOrderByDiningTableId(Long diningTableId) {
        log.info("Request get order by dining table id {}", diningTableId);
        Invoice invoiceFirstInTable = invoiceRepository.findFirstByDiningTableIdOrderByCreatedAtDesc(diningTableId);
        //neu hoa don khong ton tai hoac da thanh toan thi tra ve null
        if(invoiceFirstInTable == null || invoiceFirstInTable.getPaymentStatus() == PaymentStatus.PAID){
            return new ArrayList<>();
        }
        //nguoc lai (hoa don nay dang trong qua trinh dat mon)
        else{
            List<Order> orders = invoiceFirstInTable.getOrders();
            log.info("Get order list by dining table id {} successfully", diningTableId);
            return orders.stream().map(order -> OrderResponse.builder()
                    .id(order.getId())
                    .orderStatus(order.getStatus())
                    .orderDetail(order.getOrderDetails().stream().map(orderDetail ->
                                    OrderDetailResponse.builder()
                                            .id(orderDetail.getId())
                                            .priceAtOrder(orderDetail.getPriceAtOrder())
                                            .quantity(orderDetail.getQuantity())
                                            .food(FoodResponse.builder()
                                                    .id(orderDetail.getFood().getId())
                                                    .imageUrl(orderDetail.getFood().getImageUrl())
                                                    .name(orderDetail.getFood().getName())
                                                    .price(orderDetail.getFood().getPrice())
                                                    .build())
                                            .build()).toList()
                            )
                    .updatedAt(order.getUpdatedAt())
                    .createdAt(order.getCreatedAt())
                    .build()).toList();
        }
    }
    private OrderDetail cartDetailToOrderDetail(CartDetail cartDetail){
        return OrderDetail.builder()
                .quantity(cartDetail.getQuantity())
                .priceAtOrder(cartDetail.getFood().getPrice())
                .food(cartDetail.getFood())
                .build();
    }
}

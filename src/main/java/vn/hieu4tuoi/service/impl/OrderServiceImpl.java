package vn.hieu4tuoi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hieu4tuoi.common.FoodStatus;
import vn.hieu4tuoi.common.OrderStatus;
import vn.hieu4tuoi.common.PaymentStatus;
import vn.hieu4tuoi.common.TableStatus;
import vn.hieu4tuoi.dto.request.order.OrderChangeStatusRequest;
import vn.hieu4tuoi.dto.request.order.OrderRequest;
import vn.hieu4tuoi.dto.respone.food.BaseFoodResponse;
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
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

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
        Invoice invoice = invoiceRepository.findFirstByDiningTableIdOrderByCreatedAtDesc(request.getDiningTableId());

        Order order = new Order();

        if(customer.getCartDetails().isEmpty()){
            throw new ResourceNotFoundException("Cart is empty");
        }

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
        diningTable.setStatus(TableStatus.ORDERING);
        invoiceRepository.save(invoice);
        customer.getCartDetails().clear();
        customerRepository.save(customer);
        log.info(" Save order successfully with id: {}", order.getId());
        return order.getId();
    }

    //get order list by dining table id
    public List<OrderResponse> getOrderByDiningTableId(String diningTableId) {
        log.info("Request get order by dining table id {}", diningTableId);
        Invoice invoiceFirstInTable = invoiceRepository.findFirstByDiningTableIdOrderByCreatedAtDesc(diningTableId);
        //neu hoa don khong ton tai hoac da thanh toan thi tra ve rỗng
        if(invoiceFirstInTable == null || invoiceFirstInTable.getPaymentStatus() == PaymentStatus.PAID){
            return new ArrayList<>();
        }
        //nguoc lai (hoa don nay dang trong qua trinh dat mon)
        else{
            List<Order> orders = invoiceFirstInTable.getOrders();
            log.info("Get order list by dining table id {} successfully", diningTableId);
            return orders.stream().map(order -> OrderResponse.builder()
                    .id(order.getId())
                    .status(order.getStatus())
                    .orderDetail(order.getOrderDetails().stream().map(orderDetail ->
                                    OrderDetailResponse.builder()
                                            .id(orderDetail.getId())
                                            .quantity(orderDetail.getQuantity())
                                            .status(orderDetail.getStatus())
                                            .totalPrice(orderDetail.getStatus().equals(OrderStatus.CANCELLED)?0:orderDetail.getPriceAtOrder() * orderDetail.getQuantity())
                                            .food(FoodResponse.builder()
                                                    .id(orderDetail.getFood().getId())
                                                    .imageUrl(orderDetail.getFood().getImageUrl())
                                                    .name(orderDetail.getFood().getName())
                                                    .price(orderDetail.getPriceAtOrder())
                                                    .build())
                                            .build()).toList()
                            )
                    .totalPrice(order.getOrderDetails().stream().mapToDouble(orderDetail -> (orderDetail.getStatus().equals(OrderStatus.CANCELLED))?0:orderDetail.getPriceAtOrder() * orderDetail.getQuantity()).sum())
                    .updatedAt(order.getUpdatedAt())
                    .createdAt(order.getCreatedAt())
                    .build()).toList();
        }
    }

    //change order detail status
    @Transactional
    @Override
    public void changeOrderDetailStatus(OrderChangeStatusRequest request) {
        log.info("Request change order detail status {} to {}", request.getId(), request.getStatus());
        OrderDetail orderDetail = orderDetailRepository.findById(request.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Order detail not found"));
        //check status
        orderDetail.setStatus(request.getStatus());

        //get order
        Order order = orderDetail.getOrder();
        //nếu tất cả các order detail koong con dang cho thi set trang thai cho order
        boolean checkOrderDetail = true;
        for (OrderDetail detail : order.getOrderDetails()) {
            if (detail.getStatus() == OrderStatus.PENDING) {
                checkOrderDetail = false;
                break;
            }
        }
        if(checkOrderDetail){
            order.setStatus(OrderStatus.DELIVERED);
        }

        //save order and order detail
        orderRepository.save(order);
        log.info("Change order detail status successfully");
    }

    //change status of order
    @Override
    @Transactional
    public void changeOrderStatus(OrderChangeStatusRequest request) {
        log.info("Request change order status {} to {}", request.getId(), request.getStatus());
        Order order = orderRepository.findById(request.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Order not found"));
        //nếu orderdetail trong order còn đang chờ thì chuyển sang da giao
        for(OrderDetail orderDetail : order.getOrderDetails()){
            if(orderDetail.getStatus() == OrderStatus.PENDING){
                orderDetail.setStatus(OrderStatus.DELIVERED);
            }
        }

        //save order
        orderRepository.save(order);
        log.info("Change order status successfully");
    }

    private OrderDetail cartDetailToOrderDetail(CartDetail cartDetail){
        //kiem tra xem food co ton tai khong
        Food food = cartDetail.getFood();
        //kiem tra food con hang
        if(food.getStatus() == FoodStatus.UNAVAILABLE){
            throw new ResourceNotFoundException(food.getName()+" is unavailable");
        }
        return OrderDetail.builder()
                .quantity(cartDetail.getQuantity())
                .priceAtOrder(cartDetail.getFood().getPrice())
                .food(cartDetail.getFood())
                .build();
    }
}

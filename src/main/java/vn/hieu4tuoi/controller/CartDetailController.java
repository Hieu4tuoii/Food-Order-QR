package vn.hieu4tuoi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hieu4tuoi.dto.request.cartDetail.CartDetailCreationRequest;
import vn.hieu4tuoi.dto.request.cartDetail.CartDetailUpdateRequest;
import vn.hieu4tuoi.dto.respone.cartDetail.CartDetailResponse;
import vn.hieu4tuoi.dto.respone.ResponseData;
import vn.hieu4tuoi.service.CartDetailService;

import java.util.List;

@RestController
@RequestMapping("/cart")
@Tag(name = "Cart Detail Controller")
@Slf4j(topic = "CART-DETAIL-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class CartDetailController {
    private final CartDetailService cartDetailService;

    @Operation(summary = "Get cart details by customer id")
    @GetMapping("/customer/{customerId}")
    public ResponseData<List<CartDetailResponse>> getCartDetailsByCustomerId(@PathVariable @Min(value = 1, message = "customerId must be equals or greater than 1") Long customerId) {
        log.info("Getting cart details for customer id: {}", customerId);
        List<CartDetailResponse> cartDetails = cartDetailService.getCartDetailsByCustomerId(customerId);
        return ResponseData.<List<CartDetailResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get customer cart details successfully")
                .data(cartDetails)
                .build();
    }

    @Operation(summary = "Add to cart")
    @PostMapping("/")
    public ResponseData<Long> addToCart(@Valid @RequestBody CartDetailCreationRequest request) {
        log.info("Request add to cart {}", request.toString());
        Long cartDetailId = cartDetailService.save(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Add to cart success", cartDetailId);
    }

    @Operation(summary = "Update cart item quantity")
    @PutMapping("/")
    public ResponseData<Void> updateCartDetail(@Valid @RequestBody CartDetailUpdateRequest request) {
        log.info("Request update cart detail {}", request.toString());
        cartDetailService.update(request);
        return new ResponseData<>(HttpStatus.OK.value(), "Update cart detail success", null);
    }

    @Operation(summary = "Remove item from cart")
    @DeleteMapping("/{id}")
    public ResponseData<Void> removeFromCart(@PathVariable @Min(value = 1, message = "cartDetailId must be equals or greater than 1") Long id) {
        log.info("Request remove from cart with id: {}", id);
        cartDetailService.delete(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Remove from cart success", null);
    }
}

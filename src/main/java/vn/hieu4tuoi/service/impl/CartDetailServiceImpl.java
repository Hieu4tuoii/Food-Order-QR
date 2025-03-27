package vn.hieu4tuoi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.hieu4tuoi.dto.request.cartDetail.CartDetailCreationRequest;
import vn.hieu4tuoi.dto.request.cartDetail.CartDetailUpdateRequest;
import vn.hieu4tuoi.dto.respone.cartDetail.CartDetailResponse;
import vn.hieu4tuoi.dto.respone.food.FoodResponse;
import vn.hieu4tuoi.exception.ResourceNotFoundException;
import vn.hieu4tuoi.model.CartDetail;
import vn.hieu4tuoi.model.Customer;
import vn.hieu4tuoi.model.Food;
import vn.hieu4tuoi.repository.CartDetailRepository;
import vn.hieu4tuoi.repository.CustomerRepository;
import vn.hieu4tuoi.repository.FoodRepository;
import vn.hieu4tuoi.service.CartDetailService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CART_DETAIL_SERVICE")
public class CartDetailServiceImpl implements CartDetailService {
    private final CartDetailRepository cartDetailRepository;
    private final CustomerRepository customerRepository;
    private final FoodRepository foodRepository;

    @Override
    public Long save(CartDetailCreationRequest request) {
        log.info("Creating new cart detail for customer id: {} and food id: {}", request.getCustomerId(), request.getFoodId());
        
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        
        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new ResourceNotFoundException("Food not found"));
        
        // Check if this customer already has this food in cart
        CartDetail existingCartDetail = cartDetailRepository.findByCustomerIdAndFoodId(
                request.getCustomerId(), request.getFoodId());
        
        if (existingCartDetail != null) {
            // Update quantity of existing cart detail
            existingCartDetail.setQuantity(existingCartDetail.getQuantity() + request.getQuantity());
            cartDetailRepository.save(existingCartDetail);
            return existingCartDetail.getId();
        } else {
            // Create new cart detail
            CartDetail cartDetail = CartDetail.builder()
                    .customer(customer)
                    .food(food)
                    .quantity(request.getQuantity())
                    .build();
                    
            cartDetailRepository.save(cartDetail);
            return cartDetail.getId();
        }
    }

    @Override
    public void update(CartDetailUpdateRequest request) {
        log.info("Updating cart detail with id: {}", request.getId());
        CartDetail cartDetail = cartDetailRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart detail not found"));
        
        cartDetail.setQuantity(request.getQuantity());
        
        // If quantity is 0, remove the item from cart
        if (request.getQuantity() == 0) {
            cartDetailRepository.delete(cartDetail);
        } else {
            cartDetailRepository.save(cartDetail);
        }
    }

    @Override
    public void delete(Long cartDetailId) {
        log.info("Deleting cart detail with id: {}", cartDetailId);
        CartDetail cartDetail = cartDetailRepository.findById(cartDetailId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart detail not found"));
                
        cartDetailRepository.delete(cartDetail);
    }

    @Override
    public List<CartDetailResponse> getCartDetailsByCustomerId(Long customerId) {
        log.info("Getting cart details for customer id: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
                
        List<CartDetail> cartDetails = cartDetailRepository.findByCustomerId(customerId);

        return cartDetails.stream()
                .map(cartDetail -> CartDetailResponse.builder()
                        .id(cartDetail.getId())
                        .quantity(cartDetail.getQuantity())
                        .food(FoodResponse.builder()
                                .id(cartDetail.getFood().getId())
                                .name(cartDetail.getFood().getName())
                                .imageUrl(cartDetail.getFood().getImageUrl())
                                .price(cartDetail.getFood().getPrice())
                                .build())
                        .createdAt(cartDetail.getCreatedAt())
                        .updatedAt(cartDetail.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}

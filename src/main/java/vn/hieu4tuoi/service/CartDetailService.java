package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.cartDetail.CartDetailCreationRequest;
import vn.hieu4tuoi.dto.request.cartDetail.CartDetailUpdateRequest;
import vn.hieu4tuoi.dto.respone.cartDetail.CartDetailResponse;

import java.util.List;

public interface CartDetailService {

    Long save(CartDetailCreationRequest request);
    
    void update(CartDetailUpdateRequest request);
    
    void delete(Long cartDetailId);
    
    List<CartDetailResponse> getCartDetailsByCustomerId(Long customerId);
    
}

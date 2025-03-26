package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.customer.CustomerCreationRequest;
import vn.hieu4tuoi.dto.request.customer.CustomerUpdateRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.customer.CustomerDetailResponse;

public interface CustomerService {
    PageResponse getCustomerList(String keyword, String sort, int page, int size);
    CustomerDetailResponse getById(Long customerId);
    Long save(CustomerCreationRequest request);
    void update(CustomerUpdateRequest request);
    void delete(Long customerId);
}

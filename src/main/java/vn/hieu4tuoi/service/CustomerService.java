package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.customer.CustomerCreationRequest;
import vn.hieu4tuoi.dto.request.customer.CustomerUpdateRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.customer.CustomerResponse;
import vn.hieu4tuoi.model.Customer;

import java.util.List;

public interface CustomerService {
    PageResponse<List<CustomerResponse>> getCustomerList(String keyword, String sort, int page, int size);
    CustomerResponse getById(Long customerId);
    Long save(CustomerCreationRequest request);
    void update(CustomerUpdateRequest request);
    void delete(Long customerId);
}

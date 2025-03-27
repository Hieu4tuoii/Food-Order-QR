package vn.hieu4tuoi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.hieu4tuoi.dto.request.customer.CustomerCreationRequest;
import vn.hieu4tuoi.dto.request.customer.CustomerUpdateRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.customer.CustomerResponse;
import vn.hieu4tuoi.exception.ResourceNotFoundException;
import vn.hieu4tuoi.model.Customer;
import vn.hieu4tuoi.repository.CustomerRepository;
import vn.hieu4tuoi.service.CustomerService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CUSTOMER_SERVICE")
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public PageResponse<List<CustomerResponse>> getCustomerList(String keyword, String sort, int page, int size) {
        log.info("Getting customers by keyword {} sort: {}, page: {}, size: {}", keyword, sort, page, size);
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createdAt");
        if(StringUtils.hasLength(sort)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()){
                String columnName = matcher.group(1);
                order = matcher.group(3).equalsIgnoreCase("asc")
                        ? new Sort.Order(Sort.Direction.ASC, columnName)
                        : new Sort.Order(Sort.Direction.DESC, columnName);
            }
        }

        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        Page<Customer> customerPage;
        if(StringUtils.hasLength(keyword)){
            keyword = "%" + keyword.toLowerCase() + "%";
            customerPage = customerRepository.searchByKeyword(keyword, pageable);
        }else{
            customerPage = customerRepository.findAll(pageable);
        }
        List<Customer> customerList = customerPage.getContent();
        
        List<CustomerResponse> customerResponseList = customerList.stream()
                .map(customer -> CustomerResponse.builder()
                        .id(customer.getId())
                        .name(customer.getName())
                        .build())
                .collect(Collectors.toList());

        log.info("Got customers by keyword {} sort: {}, page: {}, size: {}", keyword, sort, page, size);
        return PageResponse.<List<CustomerResponse>>builder()
                .pageNo(page + 1)
                .pageSize(size)
                .totalPage(customerPage.getTotalPages())
                .items(customerResponseList)
                .build();
    }

    @Override
    public CustomerResponse getById(Long customerId) {
        log.info("Getting customer by id {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
                
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .build();
    }

    @Override
    public Long save(CustomerCreationRequest request) {
        log.info("Creating new customer: {}", request.getName());
        Customer customer = Customer.builder()
                .name(request.getName())
                .build();
                
        customerRepository.save(customer);
        return customer.getId();
    }

    @Override
    public void update(CustomerUpdateRequest request) {
        log.info("Updating customer with id: {}", request.getId());
        Customer customer = customerRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
                
        customer.setName(request.getName());
        customerRepository.save(customer);
    }

    @Override
    public void delete(Long customerId) {
        log.info("Deleting customer with id: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
                
        customerRepository.delete(customer);
    }
}

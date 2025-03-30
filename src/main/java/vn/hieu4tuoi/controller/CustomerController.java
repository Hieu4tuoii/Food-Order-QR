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
import vn.hieu4tuoi.dto.request.customer.CustomerCreationRequest;
import vn.hieu4tuoi.dto.request.customer.CustomerUpdateRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.ResponseData;
import vn.hieu4tuoi.dto.respone.customer.CustomerResponse;
import vn.hieu4tuoi.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/customer")
@Tag(name = "Customer Controller")
@Slf4j(topic = "CUSTOMER-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerService customerService;
    
//    @Operation(summary = "Find customer detail by id")
//    @GetMapping("/{id}")
//    public ResponseData<CustomerResponse> getCustomerDetail(@PathVariable @Min(value = 1, message = "customerId must be equals or greater than 1") Long id) {
//        log.info("Getting customer detail by id: {}", id);
//        CustomerResponse customerResponse = customerService.getById(id);
//        return ResponseData.<CustomerResponse>builder()
//                .status(HttpStatus.OK.value())
//                .message("Get customer detail successfully")
//                .data(customerResponse)
//                .build();
//    }

    @Operation(summary = "Save customer")
    @PostMapping("/")
    public ResponseData<Long> saveCustomer(@Valid @RequestBody CustomerCreationRequest request) {
        log.info("Request save customer {}", request.toString());
        Long customerId = customerService.save(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Save customer success", customerId);
    }

//    @Operation(summary = "Update customer")
//    @PutMapping("/")
//    public ResponseData<Void> updateCustomer(@Valid @RequestBody CustomerUpdateRequest request) {
//        log.info("Request update customer {}", request.toString());
//        customerService.update(request);
//        return new ResponseData<>(HttpStatus.OK.value(), "Update customer success", null);
//    }
//
//    @Operation(summary = "Delete customer by id")
//    @DeleteMapping("/{id}")
//    public ResponseData<Void> deleteCustomer(@PathVariable @Min(value = 1, message = "customerId must be equals or greater than 1") Long id) {
//        log.info("Request delete customer with id: {}", id);
//        customerService.delete(id);
//        return new ResponseData<>(HttpStatus.OK.value(), "Delete customer success", null);
//    }
//
//    @Operation(summary = "Get customer list by keyword, sort, page, size",
//            description = "keyword: search term (optional), sort: sort by column and direction (e.g. name:asc) (optional), page: (default 1), size: (default 10)")
//    @GetMapping("/")
//    public ResponseData<PageResponse<List<CustomerResponse>>> getCustomerList(@RequestParam(value = "keyword", required = false) String keyword,
//                                                                              @RequestParam(value = "page", defaultValue = "1") int page,
//                                                                              @RequestParam(value = "size", defaultValue = "10") int size,
//                                                                              @RequestParam(value = "sort", required = false) String sort) {
//        log.info("Getting customer list by keyword {} sort: {}, page: {}, size: {}", keyword, sort, page, size);
//        return new ResponseData<>(HttpStatus.OK.value(), "Get customer list success",
//                customerService.getCustomerList(keyword, sort, page, size));
//    }
}

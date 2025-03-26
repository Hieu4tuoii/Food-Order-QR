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
import vn.hieu4tuoi.dto.request.invoice.InvoiceCreationRequest;
import vn.hieu4tuoi.dto.request.invoice.InvoiceUpdateRequest;
import vn.hieu4tuoi.dto.respone.invoice.InvoiceDetailResponse;
import vn.hieu4tuoi.dto.respone.ResponseData;
import vn.hieu4tuoi.service.InvoiceService;

import java.util.List;

@RestController
@RequestMapping("/invoice")
@Tag(name = "Invoice Controller")
@Slf4j(topic = "INVOICE-CONTROLLER")
@RequiredArgsConstructor
@Validated
public class InvoiceController {
    private final InvoiceService invoiceService;
    
    @Operation(summary = "Find invoice detail by id")
    @GetMapping("/{id}")
    public ResponseData<?> getInvoiceDetail(@PathVariable @Min(value = 1, message = "invoiceId must be equals or greater than 1") Long id) {
        log.info("Getting invoice detail by id: {}", id);
        InvoiceDetailResponse invoiceDetailResponse = invoiceService.getById(id);
        return ResponseData.<InvoiceDetailResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get invoice detail successfully")
                .data(invoiceDetailResponse)
                .build();
    }

    @Operation(summary = "Get invoices by customer id")
    @GetMapping("/customer/{customerId}")
    public ResponseData<?> getInvoicesByCustomerId(@PathVariable @Min(value = 1, message = "customerId must be equals or greater than 1") Long customerId) {
        log.info("Getting invoices for customer id: {}", customerId);
        List<InvoiceDetailResponse> invoices = invoiceService.getInvoicesByCustomerId(customerId);
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Get customer invoices successfully")
                .data(invoices)
                .build();
    }

    @Operation(summary = "Save invoice")
    @PostMapping("/")
    public ResponseData<?> saveInvoice(@Valid @RequestBody InvoiceCreationRequest request) {
        log.info("Request save invoice {}", request.toString());
        Long invoiceId = invoiceService.save(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Save invoice success", invoiceId);
    }

    @Operation(summary = "Update invoice")
    @PutMapping("/")
    public ResponseData<?> updateInvoice(@Valid @RequestBody InvoiceUpdateRequest request) {
        log.info("Request update invoice {}", request.toString());
        invoiceService.update(request);
        return new ResponseData<>(HttpStatus.OK.value(), "Update invoice success", null);
    }

    @Operation(summary = "Delete invoice by id")
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteInvoice(@PathVariable @Min(value = 1, message = "invoiceId must be equals or greater than 1") Long id) {
        log.info("Request delete invoice with id: {}", id);
        invoiceService.delete(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Delete invoice success", null);
    }

    @Operation(summary = "Get invoice list by customer name, sort, page, size",
            description = "keyword: search by customer name (optional), sort: sort by column and direction (e.g. createdAt:desc) (optional), page: (default 1), size: (default 10)")
    @GetMapping("/")
    public ResponseData<?> getInvoiceList(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                         @RequestParam(value = "sort", required = false) String sort) {
        log.info("Getting invoice list by keyword {} sort: {}, page: {}, size: {}", keyword, sort, page, size);
        return new ResponseData<>(HttpStatus.OK.value(), "Get invoice list success", 
                invoiceService.getInvoiceList(keyword, sort, page, size));
    }
}

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
import vn.hieu4tuoi.dto.respone.ResponseData;
import vn.hieu4tuoi.dto.respone.invoice.InvoiceResponse;
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
    public ResponseData<InvoiceResponse> getInvoiceDetail(@PathVariable @Min(value = 1, message = "invoiceId must be equals or greater than 1") Long id) {
        log.info("Getting invoice detail by id: {}", id);
        InvoiceResponse invoiceResponse = invoiceService.getById(id);
        return ResponseData.<InvoiceResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Get invoice detail successfully")
                .data(invoiceResponse)
                .build();
    }

    @Operation(summary = "Get invoices by customer id")
    @GetMapping("/customer/{customerId}")
    public ResponseData<List<InvoiceResponse>> getInvoicesByCustomerId(@PathVariable @Min(value = 1, message = "customerId must be equals or greater than 1") Long customerId) {
        log.info("Getting invoices for customer id: {}", customerId);
        List<InvoiceResponse> invoices = invoiceService.getInvoicesByCustomerId(customerId);
        return ResponseData.<List<InvoiceResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get customer invoices successfully")
                .data(invoices)
                .build();
    }

    @Operation(summary = "Save invoice")
    @PostMapping("/")
    public ResponseData<Long> saveInvoice(@Valid @RequestBody InvoiceCreationRequest request) {
        log.info("Request save invoice {}", request.toString());
        Long invoiceId = invoiceService.save(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Save invoice success", invoiceId);
    }

    @Operation(summary = "Update invoice")
    @PutMapping("/")
    public ResponseData<Void> updateInvoice(@Valid @RequestBody InvoiceUpdateRequest request) {
        log.info("Request update invoice {}", request.toString());
        invoiceService.update(request);
        return new ResponseData<>(HttpStatus.OK.value(), "Update invoice success", null);
    }

    @Operation(summary = "Delete invoice by id")
    @DeleteMapping("/{id}")
    public ResponseData<Void> deleteInvoice(@PathVariable @Min(value = 1, message = "invoiceId must be equals or greater than 1") Long id) {
        log.info("Request delete invoice with id: {}", id);
        invoiceService.delete(id);
        return new ResponseData<>(HttpStatus.OK.value(), "Delete invoice success", null);
    }

}

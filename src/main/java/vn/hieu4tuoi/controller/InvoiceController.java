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
import vn.hieu4tuoi.dto.request.invoice.PaymentStatusChangeRequest;
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
    
//    @Operation(summary = "Find invoice detail by id")
//    @GetMapping("/{id}")
//    public ResponseData<InvoiceResponse> getInvoiceDetail(@PathVariable @Min(value = 1, message = "invoiceId must be equals or greater than 1") Long id) {
//        log.info("Getting invoice detail by id: {}", id);
//        InvoiceResponse invoiceResponse = invoiceService.getById(id);
//        return ResponseData.<InvoiceResponse>builder()
//                .status(HttpStatus.OK.value())
//                .message("Get invoice detail successfully")
//                .data(invoiceResponse)
//                .build();
//    }

    @Operation(summary = "Get invoices by customer id", description = "Lấy danh sách hóa đơn theo id khách hàng")
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

    @Operation(summary = "confirmPayment", description = "Xác nhận thanh toán hóa đơn ( bao gồm cập nhật trạng thái thanh toán và phương thức thanh toán")
    @PatchMapping("/confirmPayment/{id}")
    public ResponseData<Void> confirmPaymentByInvoiceId( @Valid @RequestBody PaymentStatusChangeRequest request) {
        log.info("Request change status method {}", request.toString());
        invoiceService.confirmPayment(request);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "change invoice status success", null);
    }

}

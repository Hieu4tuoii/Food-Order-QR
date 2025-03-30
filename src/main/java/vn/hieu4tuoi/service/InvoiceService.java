package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.invoice.InvoiceCreationRequest;
import vn.hieu4tuoi.dto.request.invoice.PaymentStatusChangeRequest;
import vn.hieu4tuoi.dto.respone.invoice.InvoiceResponse;

import java.util.List;

public interface InvoiceService {

    InvoiceResponse getById(Long invoiceId);
    
    Long save(InvoiceCreationRequest request);
    
//    void update(PaymentMethodChangeRequest request);
    void changePaymentMethod(PaymentMethodChangeRequest request);
    void changePaymentStatus(PaymentStatusChangeRequest request);
    
    void delete(Long invoiceId);
    
    List<InvoiceResponse> getInvoicesByCustomerId(Long customerId);
}

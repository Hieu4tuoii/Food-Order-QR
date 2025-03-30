package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.invoice.InvoiceCreationRequest;
import vn.hieu4tuoi.dto.request.invoice.PaymentStatusChangeRequest;
import vn.hieu4tuoi.dto.respone.invoice.InvoiceResponse;

import java.util.List;

public interface InvoiceService {

    InvoiceResponse getById(String invoiceId);

    String save(InvoiceCreationRequest request);
    
//    void update(PaymentMethodChangeRequest request);
    //void changePaymentMethod(PaymentMethodChangeRequest request);
    void confirmPayment(PaymentStatusChangeRequest request);
    
    void delete(String invoiceId);
    
    List<InvoiceResponse> getInvoicesByCustomerId(Long customerId);
}

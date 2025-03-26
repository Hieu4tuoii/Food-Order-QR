package vn.hieu4tuoi.service;

import vn.hieu4tuoi.dto.request.invoice.InvoiceCreationRequest;
import vn.hieu4tuoi.dto.request.invoice.InvoiceUpdateRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.invoice.InvoiceDetailResponse;

import java.util.List;

public interface InvoiceService {
    PageResponse getInvoiceList(String keyword, String sort, int page, int size);
    
    InvoiceDetailResponse getById(Long invoiceId);
    
    Long save(InvoiceCreationRequest request);
    
    void update(InvoiceUpdateRequest request);
    
    void delete(Long invoiceId);
    
    List<InvoiceDetailResponse> getInvoicesByCustomerId(Long customerId);
}

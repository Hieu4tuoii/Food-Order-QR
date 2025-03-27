package vn.hieu4tuoi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.hieu4tuoi.dto.request.invoice.InvoiceCreationRequest;
import vn.hieu4tuoi.dto.request.invoice.InvoiceUpdateRequest;
import vn.hieu4tuoi.dto.respone.PageResponse;
import vn.hieu4tuoi.dto.respone.invoice.InvoiceResponse;
import vn.hieu4tuoi.exception.ResourceNotFoundException;
import vn.hieu4tuoi.model.Customer;
import vn.hieu4tuoi.model.Invoice;
import vn.hieu4tuoi.repository.CustomerRepository;
import vn.hieu4tuoi.repository.InvoiceRepository;
import vn.hieu4tuoi.service.InvoiceService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "INVOICE_SERVICE")
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;


    @Override
    public InvoiceResponse getById(Long invoiceId) {
        log.info("Getting invoice by id {}", invoiceId);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
                
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .paymentStatus(invoice.getPaymentStatus())
                .paymentMethod(invoice.getPaymentMethod())
                .customerId(invoice.getCustomer().getId())
                .customerName(invoice.getCustomer().getName())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }

    @Override
    public Long save(InvoiceCreationRequest request) {
        log.info("Creating new invoice for customer id: {}", request.getCustomerId());
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
                
        Invoice invoice = Invoice.builder()
                .customer(customer)
                .build();
                
        invoiceRepository.save(invoice);
        return invoice.getId();
    }

    @Override
    public void update(InvoiceUpdateRequest request) {
        log.info("Updating invoice with id: {}", request.getId());
        Invoice invoice = invoiceRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        
        invoice.setPaymentStatus(request.getPaymentStatus());
        invoice.setPaymentMethod(request.getPaymentMethod());
        
        invoiceRepository.save(invoice);
    }

    @Override
    public void delete(Long invoiceId) {
        log.info("Deleting invoice with id: {}", invoiceId);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
                
        invoiceRepository.delete(invoice);
    }

    @Override
    public List<InvoiceResponse> getInvoicesByCustomerId(Long customerId) {
        log.info("Getting invoices for customer id: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
                
        List<Invoice> invoices = invoiceRepository.findByCustomerId(customerId);
        
        return invoices.stream()
                .map(invoice -> InvoiceResponse.builder()
                        .id(invoice.getId())
                        .paymentStatus(invoice.getPaymentStatus())
                        .paymentMethod(invoice.getPaymentMethod())
                        .customerId(invoice.getCustomer().getId())
                        .customerName(invoice.getCustomer().getName())
                        .createdAt(invoice.getCreatedAt())
                        .updatedAt(invoice.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}

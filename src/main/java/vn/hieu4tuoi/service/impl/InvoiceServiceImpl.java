package vn.hieu4tuoi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hieu4tuoi.common.OrderStatus;
import vn.hieu4tuoi.common.PaymentStatus;
import vn.hieu4tuoi.common.TableStatus;
import vn.hieu4tuoi.dto.request.invoice.InvoiceCreationRequest;
import vn.hieu4tuoi.dto.request.invoice.PaymentStatusChangeRequest;
import vn.hieu4tuoi.dto.respone.customer.CustomerResponse;
import vn.hieu4tuoi.dto.respone.invoice.InvoiceItemResponse;
import vn.hieu4tuoi.dto.respone.invoice.InvoiceResponse;
import vn.hieu4tuoi.exception.ResourceNotFoundException;
import vn.hieu4tuoi.model.Customer;
import vn.hieu4tuoi.model.DiningTable;
import vn.hieu4tuoi.model.Invoice;
import vn.hieu4tuoi.repository.CustomerRepository;
import vn.hieu4tuoi.repository.DiningTableRepository;
import vn.hieu4tuoi.repository.InvoiceRepository;
import vn.hieu4tuoi.service.InvoiceService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "INVOICE_SERVICE")
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final DiningTableRepository diningTableRepository;

    @Override
    public InvoiceResponse getById(String invoiceId) {
        log.info("Getting invoice by id {}", invoiceId);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        //tính tổng số lượng cua mỗi món ăn
        List<InvoiceItemResponse> invoiceItemResponseList = invoiceRepository.findInvoiceItemsByInvoiceId(invoiceId, OrderStatus.DELIVERED);
                
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .paymentStatus(invoice.getPaymentStatus())
                .paymentMethod(invoice.getPaymentMethod())
                .items(invoiceItemResponseList)
                .customer(CustomerResponse.builder()
                        .id(invoice.getCustomer().getId())
                        .name(invoice.getCustomer().getName())
                        .build())
                .totalPrice(invoiceItemResponseList.stream()
                        .mapToDouble(InvoiceItemResponse::getTotalPrice) // Lấy tổng giá trị
                        .sum())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .dinningTableName(invoice.getDiningTable().getName())
                .build();
    }

//    @Override
//    public String save(InvoiceCreationRequest request) {
//        log.info("Creating new invoice for customer id: {}", request.getCustomerId());
//        Customer customer = customerRepository.findById(request.getCustomerId())
//                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
//        DiningTable dinningTable = diningTableRepository.findById(request.getDiningTableId())
//                .orElseThrow(() -> new ResourceNotFoundException("Dining table not found"));
//
//        Invoice invoice = Invoice.builder()
//                .customer(customer)
//                .diningTable(dinningTable)
//                .build();
//
//        invoiceRepository.save(invoice);
//        return invoice.getId();
//    }

//    @Override
//    public void changePaymentMethod(PaymentMethodChangeRequest request) {
//        log.info("change invoice method with id: {}", request.getId());
//        Invoice invoice = invoiceRepository.findById(request.getId())
//                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
//        invoice.setPaymentMethod(request.getPaymentMethod());
//
//        log.info("change invoice method with id: {}", request.getId());
//        invoiceRepository.save(invoice);
//    }

    @Transactional
    @Override
    public void confirmPayment(PaymentStatusChangeRequest request) {
        log.info("change invoice status with id: {} and paymentMethod {}", request.getId(), request.getPaymentMethod());
        Invoice invoice = invoiceRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        invoice.setPaymentStatus(PaymentStatus.PAID);
        invoice.setPaymentMethod(request.getPaymentMethod());

        //update table status
        DiningTable diningTable = invoice.getDiningTable();
        diningTable.setStatus(TableStatus.EMPTY);

        log.info("change invoice status with id: {} and paymentMethod {}", request.getId(), request.getPaymentMethod());
        invoiceRepository.save(invoice);
    }


//    @Override
//    public void delete(String invoiceId) {
//        log.info("Deleting invoice with id: {}", invoiceId);
//        Invoice invoice = invoiceRepository.findById(invoiceId)
//                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
//
//        invoiceRepository.delete(invoice);
//    }

    @Override
    public List<InvoiceResponse> getInvoicesByCustomerId(Long customerId) {
        log.info("Getting invoices for customer id: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        List<Invoice> invoices = invoiceRepository.findByCustomerId(customerId);


        return invoices.stream()
                .map(invoice -> {
                    List<InvoiceItemResponse> invoiceItemResponseList = invoiceRepository.findInvoiceItemsByInvoiceId(invoice.getId(), OrderStatus.DELIVERED);
                    return InvoiceResponse.builder()
                            .id(invoice.getId())
                            .paymentStatus(invoice.getPaymentStatus())
                            .paymentMethod(invoice.getPaymentMethod())
                            .items(invoiceItemResponseList)
                            .customer(CustomerResponse.builder()
                                    .id(invoice.getCustomer().getId())
                                    .name(invoice.getCustomer().getName())
                                    .build())
                            .totalPrice(invoiceItemResponseList.stream()
                                    .mapToDouble(InvoiceItemResponse::getTotalPrice)
                                    .sum())
                            .createdAt(invoice.getCreatedAt())
                            .updatedAt(invoice.getUpdatedAt())
                            .dinningTableName(invoice.getDiningTable().getName())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceResponse getCurrentTableInvoice(String tableId) {
        log.info("Getting current invoice for table id: {}", tableId);
        DiningTable diningTable = diningTableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Dining table not found"));
        //lay ra invoice chua thanh toan cua bàn
        Invoice invoice = invoiceRepository.findFirstByDiningTableIdAndPaymentStatus(tableId, PaymentStatus.UNPAID);
        if (invoice == null) {
            throw new ResourceNotFoundException("Invoice not found");
        }

        //tính tổng số lượng cua mỗi món ăn
        List<InvoiceItemResponse> invoiceItemResponseList = invoiceRepository.findInvoiceItemsByInvoiceId(invoice.getId(), OrderStatus.DELIVERED);

        return InvoiceResponse.builder()
                .id(invoice.getId())
                .paymentStatus(invoice.getPaymentStatus())
                .paymentMethod(invoice.getPaymentMethod())
                .items(invoiceItemResponseList)
                .customer(CustomerResponse.builder()
                        .id(invoice.getCustomer().getId())
                        .name(invoice.getCustomer().getName())
                        .build())
                .totalPrice(invoiceItemResponseList.stream()
                        .mapToDouble(InvoiceItemResponse::getTotalPrice) // Lấy tổng giá trị
                        .sum())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .dinningTableName(invoice.getDiningTable().getName())
                .build();
    }
}

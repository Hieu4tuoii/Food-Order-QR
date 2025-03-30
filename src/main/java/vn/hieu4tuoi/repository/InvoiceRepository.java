package vn.hieu4tuoi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hieu4tuoi.common.OrderStatus;
import vn.hieu4tuoi.common.PaymentStatus;
import vn.hieu4tuoi.dto.respone.invoice.InvoiceItemResponse;
import vn.hieu4tuoi.model.Invoice;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query(value = "select i from Invoice i join fetch i.customer c where lower(c.name) like :keyword")
    Page<Invoice> searchByCustomerNameKeyword(@Param("keyword") String keyword, Pageable pageable);

    Invoice findFirstByDiningTableIdOrderByCreatedAtDesc(Long diningTableId);

    List<Invoice> findByCustomerId(Long customerId);

    @Query("SELECT new vn.hieu4tuoi.dto.respone.invoice.InvoiceItemResponse( " +
            "f.id, f.name, f.imageUrl, od.priceAtOrder, SUM(od.quantity), SUM(od.quantity) * od.priceAtOrder) " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "JOIN o.invoice i " +
            "JOIN od.food f " +
            "WHERE i.id = :invoiceId and od.status = :status " +
            "GROUP BY f.id, f.name, f.imageUrl, od.priceAtOrder")
    List<InvoiceItemResponse> findInvoiceItemsByInvoiceId(@Param("invoiceId") Long invoiceId, @Param("status") OrderStatus status);}

package vn.hieu4tuoi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hieu4tuoi.common.PaymentStatus;
import vn.hieu4tuoi.model.Invoice;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query(value = "select i from Invoice i join fetch i.customer c where lower(c.name) like :keyword")
    Page<Invoice> searchByCustomerNameKeyword(@Param("keyword") String keyword, Pageable pageable);

    Invoice findFirstByDiningTableIdOrderByCreatedAtDesc(Long diningTableId);
    
    List<Invoice> findByCustomerId(Long customerId);
}

package vn.hieu4tuoi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hieu4tuoi.model.CartDetail;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    @Query(value = "select cd from CartDetail cd join fetch cd.customer c where lower(c.name) like :keyword")
    Page<CartDetail> searchByCustomerNameKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    List<CartDetail> findByCustomerId(Long customerId);
    
    List<CartDetail> findByFoodId(Long foodId);
    
    @Query(value = "select cd from CartDetail cd where cd.customer.id = :customerId and cd.food.id = :foodId")
    CartDetail findByCustomerIdAndFoodId(@Param("customerId") Long customerId, @Param("foodId") Long foodId);

    //xoa cartdetail theo foodid
    void deleteByFoodId(Long foodId);
}

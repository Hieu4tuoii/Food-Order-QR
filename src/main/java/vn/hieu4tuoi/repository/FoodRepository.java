package vn.hieu4tuoi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hieu4tuoi.model.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    @Query(value = "select f from Food f where lower(f.name) like :keyword")
    Page<Food> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select f from Food f where f.name like :keyword and f.category.id = :categoryId")
    Page<Food> searchByKeywordAndCategoryId(@Param("categoryId") Long categoryId, @Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select f from Food f where f.category.id = :categoryId")
    Page<Food> searchByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    // Additional query methods can be added here if needed
}

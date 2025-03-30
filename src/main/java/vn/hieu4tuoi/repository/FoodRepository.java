package vn.hieu4tuoi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hieu4tuoi.common.FoodStatus;
import vn.hieu4tuoi.model.Food;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    @Query(value = "select f from Food f where lower(f.name) like :keyword and f.status in (:statuses) ")
    Page<Food> searchByKeyword(@Param("keyword") String keyword, Pageable pageable, @Param("statuses") List<FoodStatus> statuses);

    @Query(value = "select f from Food f where f.name like :keyword and f.category.id = :categoryId and f.status in (:statuses)")
    Page<Food> searchByKeywordAndCategoryId(@Param("categoryId") Long categoryId, @Param("keyword") String keyword, Pageable pageable, @Param("statuses") List<FoodStatus> statuses);

    @Query(value = "select f from Food f where f.category.id = :categoryId and f.status in (:statuses)")
    Page<Food> searchByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable, @Param("statuses") List<FoodStatus> statuses);

    //findall and status in
    Page<Food> findAllByStatusIn(@Param("statuses") List<FoodStatus> statuses, Pageable pageable);

    Optional<Food> findByIdAndStatusIn(Long id, List<FoodStatus> statuses);

    //remove food


}

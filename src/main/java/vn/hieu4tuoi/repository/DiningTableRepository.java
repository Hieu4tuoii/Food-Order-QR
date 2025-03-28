package vn.hieu4tuoi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hieu4tuoi.model.DiningTable;

import java.util.List;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {
    @Query("SELECT dt FROM DiningTable dt WHERE LOWER(dt.name) LIKE :keyword")
    List<DiningTable> searchByKeyword(@Param("keyword") String keyword);
}

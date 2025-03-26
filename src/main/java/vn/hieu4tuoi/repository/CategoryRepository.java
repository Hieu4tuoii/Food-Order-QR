package vn.hieu4tuoi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hieu4tuoi.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

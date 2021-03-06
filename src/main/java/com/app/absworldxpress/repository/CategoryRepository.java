package com.app.absworldxpress.repository;

import com.app.absworldxpress.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, String> {
    boolean existsByCatId(String catId);
}

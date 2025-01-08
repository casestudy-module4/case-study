package com.example.casestudy.repository;

import com.example.casestudy.dto.CategoryDTO;
import com.example.casestudy.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
        Page<Category> findAllByNameCategoryContainingIgnoreCase(String nameCategory, Pageable pageable);

        @Query("SELECT new com.example.casestudy.dto.CategoryDTO(c.id, c.nameCategory, " +
                "(SELECT p.image FROM products p WHERE p.category.id = c.id ORDER BY p.id ASC LIMIT 1)) " +
                "FROM categories c")
        List<CategoryDTO> findAllCategoryDTOs();
}

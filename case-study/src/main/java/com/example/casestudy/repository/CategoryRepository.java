package com.example.casestudy.repository;

import com.example.casestudy.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
        Page<Category> findAllByNameCategoryContainingIgnoreCase(String nameCategory, Pageable pageable);
}

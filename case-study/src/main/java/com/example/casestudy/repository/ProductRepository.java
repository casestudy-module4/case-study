package com.example.casestudy.repository;

import com.example.casestudy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM products p WHERE LOWER(p.category.nameCategory) LIKE LOWER(CONCAT('%', :nameCategory, '%'))")
    Page<Product> findAllByCategory_nameCategoryContainingIgnoreCase(@Param("nameCategory") String nameCategory, Pageable pageable);
    List<Product> findByCategory_Id(Integer category);
}


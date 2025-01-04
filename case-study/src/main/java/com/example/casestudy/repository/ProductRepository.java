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
    @Query("SELECT p.name, SUM(o.quantity) FROM details_order o JOIN o.product p GROUP BY p.id, p.name ORDER BY SUM(o.quantity) DESC")
    List<Object[]> findMostPurchasedProducts();

    @Query("SELECT MONTH(o.timeOrder) AS month, SUM(d.quantity) AS totalSold " +
            "FROM details_order d JOIN d.order o " +
            "GROUP BY MONTH(o.timeOrder) " +
            "ORDER BY MONTH(o.timeOrder)")
    List<Object[]> findSalesByMonth();
    @Query("SELECT p FROM products p WHERE LOWER(p.category.nameCategory) LIKE LOWER(CONCAT('%', :nameCategory, '%'))")
    Page<Product> findAllByCategory_nameCategoryContainingIgnoreCase(@Param("nameCategory") String nameCategory, Pageable pageable);
    List<Product> findByCategory_Id(Integer category);
}

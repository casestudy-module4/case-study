package com.example.casestudy.repository;

import com.example.casestudy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p.name, SUM(o.quantity) FROM details_order o JOIN o.product p GROUP BY p.id, p.name ORDER BY SUM(o.quantity) DESC")
    List<Object[]> findMostPurchasedProducts();
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);

    @Query("SELECT MONTH(o.timeOrder) AS month, SUM(d.quantity) AS totalSold " +
            "FROM details_order d JOIN d.order o " +
            "GROUP BY MONTH(o.timeOrder) " +
            "ORDER BY MONTH(o.timeOrder)")
    List<Object[]> findSalesByMonth();
    @Query("SELECT p FROM products p WHERE LOWER(p.category.nameCategory) LIKE LOWER(CONCAT('%', :nameCategory, '%'))")
    Page<Product> findAllByCategory_nameCategoryContainingIgnoreCase(@Param("nameCategory") String nameCategory, Pageable pageable);
    List<Product> findByCategory_Id(Integer category);
    @Query("SELECT p.totalProductQuantity - COALESCE(SUM(od.quantity), 0) " +
            "FROM products p " +
            "LEFT JOIN details_order od ON p.id = od.product.id " +
            "LEFT JOIN payments  pay ON od.order.id = pay.order.id " +
            "WHERE p.id = :productId AND pay.status = com.example.casestudy.model.Payment.PaymentStatus.COMPLETED " +
            "GROUP BY p.id")
    Integer findRemainProductQuantity(@Param("productId") Integer productId);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);


    @Query("SELECT p FROM products p JOIN details_order d ON p.id = d.product.id " +
            "GROUP BY p.id ORDER BY SUM(d.quantity) DESC")
    List<Product> findTopSellingProducts();

    List<Product> findTop4ByOrderByIdAsc();
}

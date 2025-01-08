package com.example.casestudy.repository;

import com.example.casestudy.dto.TopProductDTO;
import com.example.casestudy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    Optional<Product> findById(Integer id);

    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);

    @Query("SELECT new com.example.casestudy.dto.TopProductDTO(p, c, SUM(od.quantity), p.image) " +
            "FROM products p " +
            "JOIN p.category c " +
            "JOIN details_order od ON od.product.id = p.id " +
            "GROUP BY p.id, p.name, p.image, c.id, c.nameCategory " +
            "ORDER BY SUM(od.quantity) DESC")

    Page<TopProductDTO> findTopSellingProducts(PageRequest pageable);
}

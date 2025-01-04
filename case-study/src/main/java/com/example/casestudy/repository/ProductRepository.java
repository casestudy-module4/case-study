package com.example.casestudy.repository;

import com.example.casestudy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p.name, SUM(o.quantity) FROM details_order o JOIN o.product p GROUP BY p.id, p.name ORDER BY SUM(o.quantity) DESC")
    List<Object[]> findMostPurchasedProducts();

    @Query("SELECT MONTH(o.timeOrder) AS month, SUM(d.quantity) AS totalSold " +
            "FROM details_order d JOIN d.order o " +
            "GROUP BY MONTH(o.timeOrder) " +
            "ORDER BY MONTH(o.timeOrder)")
    List<Object[]> findSalesByMonth();
}

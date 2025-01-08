package com.example.casestudy.repository;

import com.example.casestudy.dto.TopProductDTO;
import com.example.casestudy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT new com.example.casestudy.dto.TopProductDTO(p, c, SUM(od.quantity), p.image) " +
            "FROM products p " +
            "JOIN p.category c " +
            "JOIN details_order od ON od.product.id = p.id " +
            "GROUP BY p.id, p.name, p.image, c.id, c.nameCategory " +
            "ORDER BY SUM(od.quantity) DESC")

    Page<TopProductDTO> findTopSellingProducts(PageRequest pageable);
}
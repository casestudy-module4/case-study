package com.example.casestudy.repository;

import com.example.casestudy.dto.ProductSalesDTO;
import com.example.casestudy.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetails, Integer> {
    @Query("SELECT new com.example.casestudy.dto.ProductSalesDTO(od.product.id, od.product.name, SUM(od.quantity)) " +
            "FROM details_order od " +
            "JOIN od.order o " +
            "JOIN payments p ON p.order.id = o.id " +
            "WHERE p.status = 'COMPLETED' " +
            "GROUP BY od.product.id, od.product.name " +
            "ORDER BY SUM(od.quantity) DESC")
    Integer calculateTotalSoldByProductWithCompletedPayments(@Param("productId") Integer productId);

}

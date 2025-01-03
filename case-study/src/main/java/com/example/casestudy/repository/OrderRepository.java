package com.example.casestudy.repository;

import com.example.casestudy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {


    @Query("SELECT c.fullName, COUNT(o.id) AS orderCount, SUM(o.totalPrice) AS totalSpent " +
            "FROM customers c JOIN oders o ON c.id = o.customer.id " +
            "GROUP BY c.id")
    List<Object[]> findCustomerOrderStatistics();

    @Query("SELECT COUNT(DISTINCT o.customer.id) FROM oders o")
    long countDistinctCustomersWithOrders();
}

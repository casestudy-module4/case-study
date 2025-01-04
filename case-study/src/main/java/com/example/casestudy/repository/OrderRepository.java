package com.example.casestudy.repository;

import com.example.casestudy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT c.fullName, COUNT(o.id) FROM orders o JOIN o.customer c GROUP BY c.id, c.fullName ORDER BY COUNT(o.id) DESC")
    List<Object[]> findCustomerWithMostOrders();

    @Query("SELECT c.fullName, SUM(o.totalPrice) FROM orders o JOIN o.customer c GROUP BY c.id, c.fullName ORDER BY SUM(o.totalPrice) DESC")
    List<Object[]> findCustomerWithHighestSpending();
}

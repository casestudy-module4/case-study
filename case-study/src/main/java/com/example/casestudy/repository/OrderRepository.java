package com.example.casestudy.repository;

import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT c.fullName, COUNT(o.id) FROM orders o JOIN o.customer c GROUP BY c.id, c.fullName ORDER BY COUNT(o.id) DESC")
    List<Object[]> findCustomerWithMostOrders();
    // Tìm đơn hàng của khách hàng theo trạng thái
    List<Order> findByCustomerIdAndStatusOrder(Integer customerId, Integer statusOrder);

    // Tìm đơn hàng theo ID của khách hàng
    List<Order> findByCustomerId(Integer customerId);

    @Query("SELECT c.fullName, SUM(o.totalPrice) FROM orders o JOIN o.customer c GROUP BY c.id, c.fullName ORDER BY SUM(o.totalPrice) DESC")
    List<Object[]> findCustomerWithHighestSpending();

}

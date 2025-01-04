package com.example.casestudy.repository;

import com.example.casestudy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Tìm đơn hàng của khách hàng theo trạng thái
    List<Order> findByCustomerIdAndStatusOrder(Integer customerId, Integer statusOrder);

    // Tìm đơn hàng theo ID của khách hàng
    List<Order> findByCustomerId(Integer customerId);

}

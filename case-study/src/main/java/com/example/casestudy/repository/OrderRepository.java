package com.example.casestudy.repository;

import com.example.casestudy.dto.OrderHistoryDTO;
import com.example.casestudy.dto.OrderItemDTO;
import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT new com.example.casestudy.dto.OrderHistoryDTO(o.id, o.timeOrder, o.statusOrder, o.totalPrice) " +
            "FROM orders o " +
            "WHERE o.customer.id = :customerId " +
            "ORDER BY o.timeOrder DESC")
    List<OrderHistoryDTO> findOrderHistoryByCustomerId(@Param("customerId") Integer customerId);

    @Query("SELECT new com.example.casestudy.dto.OrderItemDTO(p.name, od.quantity, od.priceDetailOrder, p.image) " +
            "FROM details_order od JOIN od.product p " +
            "WHERE od.order.id = :orderId")
    List<OrderItemDTO> findOrderItemsByOrderId(@Param("orderId") Integer orderId);

    @Query("SELECT new com.example.casestudy.dto.OrderHistoryDTO(o.id, o.timeOrder, o.statusOrder, o.totalPrice) " +
            "FROM orders o " +
            "WHERE o.customer.id = :customerId AND o.statusOrder = :statusOrder " +
            "ORDER BY o.timeOrder DESC")
    List<OrderHistoryDTO> findOrderHistoryByCustomerIdAndStatusOrder(int customerId, Integer statusOrder);

}

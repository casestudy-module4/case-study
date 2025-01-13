package com.example.casestudy.repository;

import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.model.Payment;
import com.example.casestudy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    Optional<OrderDetails> findByProduct(Product product);

    List<OrderDetails> findByOrderId(Integer orderId);

    List<OrderDetails> findAllByOrderCustomerId(Integer customerId);

    @Query("SELECT SUM(od.priceDetailOrder) FROM details_order od WHERE od.order.id = :orderId")
    Double calculateTotalPriceByOrderId(Integer orderId);
    List<OrderDetails> findByOrder(Order order);
    Optional<OrderDetails> findByProductId(Integer productId);
    @Query("SELECT od.product.id FROM details_order od " +
            "JOIN od.order o " +
            "WHERE o.id = (SELECT p.order.id FROM payments p WHERE p.id = ?1)")
    Optional<Integer> findProductIdByPaymentId(Integer paymentId);
    @Query("SELECT od.quantity, od.product.id AS paymentId " +
            "FROM details_order od " +
            "JOIN od.order o " +
            "JOIN payments p ON p.order.id = o.id " +
            "JOIN od.product pr " +
            "WHERE p.status = 'COMPLETED'")
    List<OrderDetails> findOrderDetailsWithPaymentAndProduct(
    );
//    @Query("SELECT od.product.id , od.quantity FROM details_order od WHERE od.order.id = ?1")
//    List<OrderDetails> findOrderDetails(Integer idOrder);

//    @Query("SELECT od.id, od.product.id, od.quantity, od.priceDetailOrder FROM details_order od WHERE od.order.id = ?1")
//    List<Object[]> findOrderDetailsWithSpecificColumns(Integer idOrder);

    @Query("SELECT od FROM details_order od WHERE od.order.id = ?1")
    List<OrderDetails> findAllOrderDetails(Integer idOrder);


}

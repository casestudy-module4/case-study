package com.example.casestudy.repository;

import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;
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



}

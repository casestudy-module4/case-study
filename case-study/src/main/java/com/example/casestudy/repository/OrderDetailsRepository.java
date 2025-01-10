package com.example.casestudy.repository;

import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    Optional<OrderDetails> findByProduct(Product product);

    List<OrderDetails> findByOrderId(Integer orderId);

    List<OrderDetails> findAllByOrderCustomerId(Integer customerId);

    @Query("SELECT SUM(od.priceDetailOrder) FROM details_order od WHERE od.order.id = :orderId")
    Double calculateTotalPriceByOrderId(Integer orderId);
    List<OrderDetails> findByOrder(Order order);
}

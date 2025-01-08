package com.example.casestudy.repository;

import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    Optional<OrderDetails> findByProduct(Product product);

    List<OrderDetails> findByOrderId(Integer orderId);
}

package com.example.casestudy.repository;

import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    Optional<OrderDetails> findByProduct(Product product);
}

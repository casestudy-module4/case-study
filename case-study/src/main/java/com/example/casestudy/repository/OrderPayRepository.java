package com.example.casestudy.repository;

import com.example.casestudy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPayRepository extends JpaRepository<Order, Integer> {
}

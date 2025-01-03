package com.example.casestudy.repository;

import com.example.casestudy.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByStatus(Payment.PaymentStatus status);
    List<Payment> findByOrderId(Integer orderId);
}

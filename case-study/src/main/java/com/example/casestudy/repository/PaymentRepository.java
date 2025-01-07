package com.example.casestudy.repository;

import com.example.casestudy.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query("SELECT p FROM payments p WHERE p.status = :status")
    List<Payment> findPaymentsByStatus(@Param("status") Payment.PaymentStatus status);
    List<Payment> findByStatus(Payment.PaymentStatus status);
    List<Payment> findByOrderId(Integer orderId);
}


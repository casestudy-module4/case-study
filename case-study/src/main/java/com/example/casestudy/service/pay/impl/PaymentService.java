package com.example.casestudy.service.pay.impl;

import com.example.casestudy.model.Order;
import com.example.casestudy.model.Payment;
import com.example.casestudy.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(Order order, Payment.PaymentMethod paymentMethod) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setAmount(order.getTotalPrice());
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(Payment.PaymentStatus.PENDING);
        return paymentRepository.save(payment);
    }

    public Payment updatePaymentStatus(Payment payment, Payment.PaymentStatus status) {
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }
}

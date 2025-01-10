//package com.example.casestudy.service.impl;
//
//import com.example.casestudy.model.Order;
//import com.example.casestudy.model.Payment;
//import com.example.casestudy.repository.OrderPayRepository;
//import com.example.casestudy.repository.PaymentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//@Service
//public class PaymentService {
//    @Autowired
//    private PaymentRepository paymentRepository;
//
//    @Autowired
//    private OrderPayRepository orderPayRepository;
//
//    public Payment createPayment(Order order, Payment.PaymentMethod paymentMethod) {
//        Payment payment = new Payment();
//        payment.setOrder(order);
//        payment.setPaymentDate(LocalDateTime.now());
//        payment.setAmount(order.getTotalPrice());
//        payment.setPaymentMethod(paymentMethod);
//        payment.setStatus(Payment.PaymentStatus.PENDING);
//        return paymentRepository.save(payment);
//    }
//
//    public Payment updatePaymentStatus(Payment payment, Payment.PaymentStatus status) {
//        payment.setStatus(status);
//
////        if (status == Payment.PaymentStatus.SUCCESS) {
////            // Tạo đơn hàng khi thanh toán thành công
////            createOrderAfterPaymentSuccess(payment);
////        }
//
//        return paymentRepository.save(payment);
//    }
//
//    private void createOrderAfterPaymentSuccess(Payment payment) {
////        Order order = payment.getOrder();
////        order.setOrderDate(LocalDateTime.now());
////        order.setStatus(Order.OrderStatus.CONFIRMED);
////        orderRepository.save(order);
//    }
//}
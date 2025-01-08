//package com.example.casestudy.service.implement;
//
//import com.example.casestudy.dto.PaymentRequest;
//import com.example.casestudy.model.Customer;
//import com.example.casestudy.model.Order;
//import com.example.casestudy.model.Payment;
//import com.example.casestudy.repository.PaymentRepository;
//import com.example.casestudy.repository.OrderRepository;
//import com.example.casestudy.service.IPayService;
//import jakarta.mail.MessagingException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class PaymentService implements IPayService {
//
//    @Autowired
//    private CustomerService customerService;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private PaymentRepository paymentRepository;
//
//    @Autowired
//    private EmailService emailService;
//
//    public boolean processPayment(PaymentRequest paymentRequest) throws MessagingException {
//        int customerId = paymentRequest.getOrderId();
//        Customer customer = customerService.findById(customerId);
//
//        if (customer == null) {
//            return false;
//        }
//        Order order = orderRepository.findById(paymentRequest.getOrderId())
//                .orElse(null);
//        if (order == null) {
//            return false;
//        }
//        boolean paymentSuccess = performPayment(paymentRequest.getAmount());
//
//        if (paymentSuccess) {
//            Payment payment = new Payment();
//            payment.setOrder(order);
//            payment.setPaymentDate(LocalDateTime.now());
//            payment.setAmount(paymentRequest.getAmount());
//            payment.setPaymentMethod(paymentRequest.getPaymentMethod());
//            payment.setStatus(Payment.PaymentStatus.COMPLETED);
//            paymentRepository.save(payment);
//            emailService.sendPaymentSuccessEmail(
//                    customer.getEmail(),
//                    customer.getFullName(),
//                    order.getId().toString(),
//                    paymentRequest.getAmount().toString()
//            );
//            return true;
//        }
//        return false;
//    }
//
//    private boolean performPayment(Double amount) {
//        return true;
//    }
//
//    public void sendPaymentSuccessEmails() {
//        List<Payment> successfulPayments = paymentRepository.findPaymentsByStatus(Payment.PaymentStatus.COMPLETED);
//
//        for (Payment payment : successfulPayments) {
//            try {
//                emailService.sendPaymentSuccessEmail(
//                        payment.getOrder().getCustomer().getEmail(),
//                        payment.getOrder().getCustomer().getFullName(),
//                        payment.getOrder().getId().toString(),
//                        payment.getAmount().toString()
//                );
//            } catch (MessagingException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    @Scheduled(cron = "0 * * * * ?")
//    public void scheduleSendEmailForSuccessfulPayments() {
//        sendPaymentSuccessEmails();
//        System.out.println("Scheduled task executed - sending emails for successful payments.");
//    }
//}
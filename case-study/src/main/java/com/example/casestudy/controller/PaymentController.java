//package com.example.casestudy.controller;
//
//import com.example.casestudy.model.Order;
//import com.paypal.api.payments.Payment;
//import com.example.casestudy.service.impl.OrderServiceImpl;
//import com.example.casestudy.service.impl.PayPalService;
//import com.example.casestudy.service.impl.PaymentService;
//import com.paypal.api.payments.Links;
//import com.paypal.base.rest.PayPalRESTException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//@RequestMapping("/paypal")
//public class PaymentController {
//    @Autowired
//    private PayPalService payPalService;
//
//    @Autowired
//    private PaymentService paymentService;
//
//    @Autowired
//    private OrderServiceImpl orderServiceImpl;
//
//    @PostMapping("/pay")
//    public String payment(@RequestParam("orderId") Integer orderId) {
//        try {
//            Order order = orderServiceImpl.getOrderById(orderId);
//            Payment payment = payPalService.createPayment(
//                    order.getTotalPrice(),
//                    "USD",
//                    "paypal",
//                    "sale",
//                    "Payment for order " + orderId,
//                    "http://localhost:8080/paypal/cancel",
//                    "http://localhost:8080/paypal/success");
//            for(Links link : payment.getLinks()) {
//                if(link.getRel().equals("approval_url")) {
//                    // Xóa hoặc comment dòng này
//                    // Payment localPayment = paymentService.createPayment(order, Payment.PaymentMethod.PAYPAL);
//                    return "redirect:"+link.getHref();
//                }
//            }
//        } catch (PayPalRESTException e) {
//            e.printStackTrace();
//        }
//        return "redirect:/";
//    }
//
//    @GetMapping("/cancel")
//    public String cancelPay() {
//        return "payment-cancel";
//    }
//
//    @GetMapping("/success")
//    public String successPay(@RequestParam("paymentId") String paymentId,
//                             @RequestParam("PayerID") String payerId, Model model) {
//        try {
//            Payment payment = payPalService.executePayment(paymentId, payerId);
//            if (payment.getState().equals("approved")) {
//                model.addAttribute("payment", payment);
//                return "payment-success";
//            }
//        } catch (PayPalRESTException e) {
//            e.printStackTrace();
//        }
//        return "redirect:/";
//    }
//}

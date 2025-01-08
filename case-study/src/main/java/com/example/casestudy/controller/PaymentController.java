package com.example.casestudy.controller;


import com.example.casestudy.dto.CartItem;
import com.example.casestudy.model.Order;
import com.example.casestudy.service.CartService;
import com.example.casestudy.service.impl.OrderServiceImpl;
import com.example.casestudy.service.impl.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("payment")
public class PaymentController {
    @Autowired
    private PayPalService payPalService;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private CartService cartService;

    private static final String SUCCESS_URL = "http://localhost:8080/payment/success";
    private static final String CANCEL_URL = "http://localhost:8080/payment/cancel";

    @PostMapping("/pay")
    public String processPayment(
            @RequestParam("amount") Double amount,
            @RequestParam("productIds") List<Integer> productIds,
            @RequestParam("quantities") List<Integer> quantities,
            HttpSession session) {
        try {

            // Lưu danh sách sản phẩm được chọn vào session
            List<CartItem> selectedItems = cartService.getSelectedItems(productIds, quantities);
            session.setAttribute("selectedItems", selectedItems);

            // Tính tổng giá trị đơn hàng
            double totalPrice = selectedItems.stream()
                    .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                    .sum();
            // Lưu thông tin thanh toán vào session
            session.setAttribute("paymentAmount", amount);


            double amountUSD = amount / 25000;
            Payment payment = payPalService.createPaymentWithPayPal(
                    amountUSD,
                    "USD",
                    "paypal",
                    "sale",
                    "Thanh toán đơn hàng",
                    CANCEL_URL,
                    SUCCESS_URL);

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
    @GetMapping("/success")
    public String successPay(@RequestParam("paymentId") String paymentId,
                             @RequestParam("PayerID") String payerId,
                             Model model,
                             HttpSession session) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                Double totalPrice = (Double) session.getAttribute("paymentAmount");
                List<CartItem> selectedItems = (List<CartItem>) session.getAttribute("selectedItems");

                Order order = new Order();
                order.setTotalPrice(totalPrice);
//                order.setStatusOrder(1);
//                Order savedOrder = orderService.saveOrder(order, selectedItems);

                // Xóa các sản phẩm được chọn khỏi giỏ hàng
                cartService.removeItems(selectedItems);

                // Xóa thông tin session sau khi xử lý
                session.removeAttribute("paymentAmount");
                session.removeAttribute("selectedItems");

//                model.addAttribute("order", savedOrder);
                return "payment-success";
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
    @GetMapping("/cancel")
    public String cancelPay() {
        return "payment-cancel";
    }
}

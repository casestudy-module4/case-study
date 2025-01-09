package com.example.casestudy.controller;

import com.example.casestudy.dto.OrderHistoryDTO;
import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.model.Payment;
import com.example.casestudy.repository.OrderRepository;
import com.example.casestudy.service.impl.OrderHistoryService;
import com.example.casestudy.service.implement.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/order-history")
public class OrderHistoryController {
    @Autowired
    private OrderHistoryService orderHistoryService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String viewOrderHistory(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerService.findByUsername(userDetails.getUsername());
        List<OrderHistoryDTO> orders = orderHistoryService.getOrderHistory(customer);
        model.addAttribute("orders", orders);
        return "order-history";
    }

    @GetMapping("/filter")
    public String filterOrders(@RequestParam Integer statusOrder, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerService.findByUsername(userDetails.getUsername());
        List<OrderHistoryDTO> orders = orderHistoryService.getOrderHistoryByStatus(customer, statusOrder);
        model.addAttribute("orders", orders);
        return "order-history";
    }

    @PostMapping("/reorder/{orderId}")
    public String reorder(@PathVariable Integer orderId, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerService.findByUsername(userDetails.getUsername());
        orderHistoryService.reorder(orderId, customer);
        return "redirect:/cart";
    }

    @PostMapping("/review/{orderId}")
    public String addReview(@PathVariable Integer orderId, @RequestParam String review, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerService.findByUsername(userDetails.getUsername());
        orderHistoryService.addReview(orderId, review, customer);
        return "redirect:/order-history";
    }
}
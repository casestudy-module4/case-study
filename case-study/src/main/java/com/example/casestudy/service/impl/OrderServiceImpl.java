package com.example.casestudy.service.impl;

import com.example.casestudy.model.Order;
import com.example.casestudy.repository.OrderPayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl {
    @Autowired
    private OrderPayRepository orderPayRepository;


    public Order getOrderById(Integer orderId) {
        return orderPayRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }


    public Order saveOrder(Order order) {
        return orderPayRepository.save(order);
    }


    public void updateOrderStatus(Integer orderId, Integer status) {
        Order order = getOrderById(orderId);
        order.setStatusOrder(status);
        orderPayRepository.save(order);
    }


    public Double getTotalAmount(Integer orderId) {
        Order order = getOrderById(orderId);
        return order.getTotalPrice();
    }
}
package com.example.casestudy.service;

import com.example.casestudy.model.Order;
import com.example.casestudy.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Tạo đơn hàng mới
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    // Lấy danh sách các đơn hàng của khách hàng
    public List<Order> getOrdersByCustomer(Integer customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    // Lấy đơn hàng theo trạng thái và ID khách hàng
    public List<Order> getOrdersByStatus(Integer customerId, Integer statusOrder) {
        return orderRepository.findByCustomerIdAndStatusOrder(customerId, statusOrder);
    }
}

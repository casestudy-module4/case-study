package com.example.casestudy.service.impl;

import com.example.casestudy.dto.CartItem;
import com.example.casestudy.model.Order;
import com.example.casestudy.repository.OrderPayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Order saveOrder(Order order, List<CartItem> cartItems) {
        // Tính tổng giá trị đơn hàng từ danh sách CartItem
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getTotalPrice() * item.getQuantity())
                .sum();
        // Thiết lập tổng giá trị và trạng thái mặc định cho đơn hàng
        order.setTotalPrice(totalAmount);
        order.setStatusOrder(0); // Giả sử 0 là trạng thái "Chờ xử lý"
        // Lưu đơn hàng vào cơ sở dữ liệu
        Order savedOrder = orderPayRepository.save(order);
        // Gọi phương thức khác nếu cần lưu CartItem vào cơ sở dữ liệu
        // e.g., cartItemRepository.saveAll(cartItems);
        return savedOrder;
    }
}
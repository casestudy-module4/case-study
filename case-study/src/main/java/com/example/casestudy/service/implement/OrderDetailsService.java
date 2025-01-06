package com.example.casestudy.service.implement;

import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.repository.OrderDetailsRepository;
import com.example.casestudy.repository.OrderRepository;
import com.example.casestudy.service.IOrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailsService  implements IOrderDetailsService {
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderDetails> getOrderDetailsByCustomerId(Integer customerId) {
        List<Order> orders = orderRepository.findByCustomerIdAndStatusOrder(customerId, 0);
        List<OrderDetails> allOrderDetails = new ArrayList<>();
        for (Order order : orders) {
            allOrderDetails.addAll(orderDetailsRepository.findByOrderId(order.getId()));
        }
        return allOrderDetails;
    }

    public int getCartItemCount(List<OrderDetails> orderDetailsList) {
        int count = 0;
        if (orderDetailsList != null) {
            for (OrderDetails orderDetails : orderDetailsList) {
                count += orderDetails.getQuantity();
            }
        }
        return count;
    }
    @Override
    public List<OrderDetails> findAll() {
        return orderDetailsRepository.findAll();
    }
}

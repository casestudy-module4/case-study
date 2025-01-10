package com.example.casestudy.service;

import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;

import java.util.List;

public interface IOrderDetailsService {
    List<OrderDetails> findAll();
    List<OrderDetails> getOrderDetailsByCustomerId(Integer customerId);
    void saveAllOrderDetails(List<OrderDetails> orderDetailsList, Order order);
    int getCartItemCount(List<OrderDetails> orderDetailsList);
}

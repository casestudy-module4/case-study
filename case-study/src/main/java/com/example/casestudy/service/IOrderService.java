package com.example.casestudy.service;

import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IOrderService {
    Map<String, Object> getCustomerWithMostOrders();

    Map<String, Object> getCustomerWithHighestSpending();

    List<Order> findAll();

    List<Order> getActiveOrderByCustomerId(Integer customerId, Integer status);

    void save(Order order);

    void addOrderDetail(OrderDetails orderDetails);

    void updateTotalPrice(Order order);
}

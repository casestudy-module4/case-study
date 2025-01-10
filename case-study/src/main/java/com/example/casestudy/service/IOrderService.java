package com.example.casestudy.service;

import com.example.casestudy.dto.OrderHistoryDTO;
import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;

import java.util.List;
import java.util.Map;

public interface IOrderService {
    Map<String, Object> getCustomerWithMostOrders();

    Map<String, Object> getCustomerWithHighestSpending();

    List<Order> findAll();
    List<OrderHistoryDTO> getOrderHistoryWithItems(Integer customerId);

    List<Order> getActiveOrderByCustomerId(Integer customerId, Integer status);

    void save(Order order);

    void addOrderDetail(OrderDetails orderDetails);

    void updateTotalPrice(Order order);

}

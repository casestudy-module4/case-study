package com.example.casestudy.service;

import com.example.casestudy.dto.OrderHistoryDTO;
import com.example.casestudy.model.Order;

import java.util.List;
import java.util.Map;

public interface IOrderService {
    Map<String, Object> getCustomerWithMostOrders();
    Map<String, Object> getCustomerWithHighestSpending();
    List<Order> findAll();
    List<OrderHistoryDTO> getOrderHistoryWithItems(Integer customerId);
}

package com.example.casestudy.service;

import java.util.List;
import java.util.Map;

public interface IOrderService {
    Map<String, Object> getCustomerWithMostOrders();
    Map<String, Object> getCustomerWithHighestSpending();
}

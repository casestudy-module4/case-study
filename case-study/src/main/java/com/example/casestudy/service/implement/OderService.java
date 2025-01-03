package com.example.casestudy.service.implement;

import com.example.casestudy.repository.OrderRepository;
import com.example.casestudy.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Object[]> getCustomerOrderStatistics() {
        return orderRepository.findCustomerOrderStatistics();
    }

    public long getDistinctCustomerCount() {
        return orderRepository.countDistinctCustomersWithOrders();
    }
}

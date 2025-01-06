package com.example.casestudy.service.implement;

import com.example.casestudy.model.Order;
import com.example.casestudy.repository.OrderRepository;
import com.example.casestudy.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Map<String, Object> getCustomerWithMostOrders() {
        List<Object[]> results = orderRepository.findCustomerWithMostOrders();
        if (!results.isEmpty()) {
            Object[] topCustomer = results.get(0);
            Map<String, Object> data = new HashMap<>();
            data.put("customerName", topCustomer[0]);
            data.put("orderCount", topCustomer[1]);
            return data;
        }
        Map<String, Object> defaultData = new HashMap<>();
        defaultData.put("customerName", "N/A");
        defaultData.put("orderCount", 0);
        return defaultData;
    }


    public Map<String, Object> getCustomerWithHighestSpending() {
        List<Object[]> results = orderRepository.findCustomerWithHighestSpending();
        if (!results.isEmpty()) {
            Object[] topSpender = results.get(0);
            Map<String, Object> data = new HashMap<>();
            data.put("customerName", topSpender[0]);
            data.put("totalSpending", topSpender[1]);
            return data;
        }
        // Trả về giá trị mặc định nếu không có dữ liệu
        Map<String, Object> defaultData = new HashMap<>();
        defaultData.put("customerName", "N/A");
        defaultData.put("totalSpending", 0);
        return defaultData;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }
}

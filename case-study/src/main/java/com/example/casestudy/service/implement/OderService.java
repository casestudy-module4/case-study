package com.example.casestudy.service.implement;

import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.repository.CustomerRepository;
import com.example.casestudy.repository.OrderDetailsRepository;
import com.example.casestudy.repository.OrderRepository;
import com.example.casestudy.service.IOrderService;
import com.example.casestudy.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private IProductService productService;
    @Autowired
    private OrderDetailsService orderDetailsService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CustomerRepository customerRepository;

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

    @Override
    public List<Order> getActiveOrderByCustomerId(Integer customerId, Integer status) {
        return orderRepository.findByCustomerIdAndStatusOrder(customerId, 1); // 1 là trạng thái giỏ hàng chưa hoàn thành
    }

    // Lưu đơn hàng
    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    // Thêm OrderDetails
    @Override
    public void addOrderDetail(OrderDetails orderDetails) {
        orderDetailsRepository.save(orderDetails);
    }

    // Cập nhật tổng giá đơn hàng
    @Override
    public void updateTotalPrice(Order order) {
        double totalPrice = orderDetailsRepository.calculateTotalPriceByOrderId(order.getId());
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }

}

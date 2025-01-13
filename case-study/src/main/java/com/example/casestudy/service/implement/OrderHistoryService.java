package com.example.casestudy.service.implement;

import com.example.casestudy.dto.OrderHistoryDTO;
import com.example.casestudy.dto.OrderItemDTO;
import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.repository.OrderDetailsRepository;
import com.example.casestudy.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderHistoryService {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;


    public List<OrderHistoryDTO> getOrderHistory(Customer customer) {
        List<OrderHistoryDTO> orderHistory = orderRepository.findOrderHistoryByCustomerId(customer.getId());
        for (OrderHistoryDTO order : orderHistory) {
            List<OrderItemDTO> items = orderRepository.findOrderItemsByOrderId(order.getId());
            order.setOrderItems(items);
        }
        return orderHistory;
    }

    public List<OrderHistoryDTO> getOrderHistoryByStatus(Customer customer, Integer statusOrder) {
        return orderRepository.findOrderHistoryByCustomerIdAndStatusOrder(customer.getId(), statusOrder);
    }

    public void reorder(Integer orderId, Customer customer) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getCustomer().getId() == customer.getId()) {
                List<OrderDetails> orderDetails = orderDetailsRepository.findByOrder(order);
                for (OrderDetails detail : orderDetails) {
                    for (int i = 0; i < detail.getQuantity(); i++) {
                        cartService.addToCart(detail.getProduct().getId());
                    }
                }
            } else {
                throw new RuntimeException("You are not authorized to reorder this order");
            }
        } else {
            throw new RuntimeException("Order not found");
        }
    }

    public void addReview(Integer orderId, String review, Customer customer) {
//        Optional<Order> orderOptional = orderRepository.findById(orderId);
//        if (orderOptional.isPresent()) {
//            Order order = orderOptional.get();
//            if (order.getCustomer().getId().equals(customer.getId())) {
//                // Implement review logic here
//                // For example:
//                // order.setReview(review);
//                // orderRepository.save(order);
//            } else {
//                throw new RuntimeException("You are not authorized to review this order");
//            }
//        } else {
//            throw new RuntimeException("Order not found");
//        }
//    }
    }
}

package com.example.casestudy.service;

import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.model.Product;
import com.example.casestudy.repository.OrderDetailsRepository;
import com.example.casestudy.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final ProductRepository productRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    public CartService(ProductRepository productRepository, OrderDetailsRepository orderDetailsRepository) {
        this.productRepository = productRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    public List<OrderDetails> getCartItems() {
        return orderDetailsRepository.findAll();
    }

    public double getCartTotal() {
        return getCartItems().stream()
                .mapToDouble(orderDetails -> orderDetails.getQuantity() * orderDetails.getPriceDetailOrder())
                .sum();
    }

    public void addToCart(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        Optional<OrderDetails> existingOrderDetail = orderDetailsRepository.findByProduct(product);
        if (existingOrderDetail.isPresent()) {
            OrderDetails orderDetails = existingOrderDetail.get();
            orderDetails.setQuantity(orderDetails.getQuantity() + 1);
            orderDetailsRepository.save(orderDetails);
        } else {
            OrderDetails newOrderDetails = new OrderDetails();
            newOrderDetails.setProduct(product);
            newOrderDetails.setQuantity(1);
            newOrderDetails.setPriceDetailOrder(product.getPrice());
            orderDetailsRepository.save(newOrderDetails);
        }
    }

    public void updateCart(Integer orderDetailId, int quantity) {
        OrderDetails orderDetails = orderDetailsRepository.findById(orderDetailId)
                .orElseThrow(() -> new RuntimeException("Chi tiết đơn hàng không tồn tại"));
        orderDetails.setQuantity(quantity);
        orderDetailsRepository.save(orderDetails);
    }

    public void removeFromCart(Integer orderDetailId) {
        orderDetailsRepository.deleteById(orderDetailId);
    }
}

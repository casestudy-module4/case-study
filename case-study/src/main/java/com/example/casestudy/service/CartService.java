package com.example.casestudy.service;

import com.example.casestudy.dto.CartItem;
import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.model.Product;
import com.example.casestudy.repository.OrderDetailsRepository;
import com.example.casestudy.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<CartItem> getSelectedItems(List<Integer> productIds, List<Integer> quantities) {
        List<CartItem> selectedItems = new ArrayList<>();
        if (productIds != null && quantities != null && productIds.size() == quantities.size()) {
            for (int i = 0; i < productIds.size(); i++) {
//                Product product = productService.getById(productIds.get(i));
                Product product = productRepository.getById(productIds.get(i));
                int quantity = quantities.get(i);
                selectedItems.add(new CartItem(product, quantity));
            }
        } else {
            selectedItems = getAllCartItems();
        }
        return selectedItems;
    }

    public List<CartItem> getAllCartItems() {
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAll();
        List<CartItem> cartItems = new ArrayList<>();

        for (OrderDetails orderDetails : orderDetailsList) {
            CartItem cartItem = new CartItem(orderDetails.getProduct(), orderDetails.getQuantity());
            cartItems.add(cartItem);
        }

        return cartItems;
    }

    public void removeItems(List<CartItem> selectedItems) {
        for (CartItem cartItem : selectedItems) {
            Product product = cartItem.getProduct();
            // Tìm OrderDetails tương ứng với sản phẩm
            Optional<OrderDetails> orderDetailsOptional = orderDetailsRepository.findByProduct(product);
            orderDetailsOptional.ifPresent(orderDetails -> orderDetailsRepository.delete(orderDetails));
        }
    }

}

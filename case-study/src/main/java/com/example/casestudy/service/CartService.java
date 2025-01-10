package com.example.casestudy.service;

import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.model.Product;
import com.example.casestudy.repository.CustomerRepository;
import com.example.casestudy.repository.OrderDetailsRepository;
import com.example.casestudy.repository.OrderRepository;
import com.example.casestudy.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // Lấy danh sách các sản phẩm trong giỏ hàng
    public List<OrderDetails> getCartItems() {
        return orderDetailsRepository.findAll();
    }

    // Tính tổng giá trị giỏ hàng
    public double getCartTotal() {
        return getCartItems().stream()
                .mapToDouble(orderDetails -> orderDetails.getQuantity() * orderDetails.getPriceDetailOrder())
                .sum();
    }

    // Thêm sản phẩm vào giỏ hàng
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

    // Cập nhật giỏ hàng
    public void updateCart(Integer orderDetailId, int quantity) {
        OrderDetails orderDetails = orderDetailsRepository.findById(orderDetailId)
                .orElseThrow(() -> new RuntimeException("Chi tiết đơn hàng không tồn tại"));
        orderDetails.setQuantity(quantity);
        orderDetailsRepository.save(orderDetails);
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeFromCart(Integer orderDetailId) {
        orderDetailsRepository.deleteById(orderDetailId);
    }

    // Lưu giỏ hàng thành đơn hàng
    public void saveCartToOrder(Integer customerId) {
        // Lấy thông tin khách hàng
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setCustomer(customer);
        order.setTimeOrder(LocalDateTime.now());
        order.setStatusOrder(0); // Giả sử 0 là trạng thái chờ xử lý
        order.setTotalPrice(getCartTotal()); // Tính tổng giá trị giỏ hàng
        orderRepository.save(order); // Lưu đơn hàng vào cơ sở dữ liệu

        // Lưu các chi tiết đơn hàng (OrderDetails)
        for (OrderDetails orderDetails : getCartItems()) {
            orderDetails.setOrder(order); // Liên kết OrderDetails với Order mới
            orderDetailsRepository.save(orderDetails); // Lưu mỗi chi tiết đơn hàng
        }

        // Xóa giỏ hàng sau khi lưu vào đơn hàng
        orderDetailsRepository.deleteAll(); // Xóa tất cả các chi tiết giỏ hàng sau khi thanh toán
    }
}

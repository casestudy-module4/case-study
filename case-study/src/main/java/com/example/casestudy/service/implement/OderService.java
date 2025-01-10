package com.example.casestudy.service.implement;

import com.example.casestudy.dto.CartItem;
import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.model.Product;
import com.example.casestudy.repository.OrderDetailsRepository;
import com.example.casestudy.repository.OrderRepository;
import com.example.casestudy.service.CartService;
import com.example.casestudy.service.IOrderService;
import com.example.casestudy.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.casestudy.config.SecurityUtils.getLoggedCustomer;

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

    @Override
    public Order saveOrder(Order order, List<OrderDetails> orderDetails) {
        Order savedOrder = orderRepository.save(order);
        for (OrderDetails detail : orderDetails) {
            detail.setOrder(savedOrder);
            orderDetailsRepository.save(detail);
        }
        return savedOrder;
    }

    @Override
    public void processCheckout(List<Integer> productIds, List<Integer> quantities, String name, String phone, String email) {
        if (productIds.isEmpty() || quantities.size() != productIds.size()) {
            throw new IllegalArgumentException("Danh sách sản phẩm hoặc số lượng không hợp lệ.");
        }

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        double totalPrice = 0;

        // Tạo OrderDetails và tính tổng tiền
        for (int i = 0; i < productIds.size(); i++) {
            Product product = productService.getById(productIds.get(i));
            int quantity = quantities.get(i);

            if (product.getRemainProductQuantity() < quantity) {
                throw new IllegalArgumentException("Sản phẩm " + product.getName() + " không đủ số lượng.");
            }

            OrderDetails details = new OrderDetails();
            details.setProduct(product);
            details.setQuantity(quantity);
            details.setPriceDetailOrder(product.getPrice());
            orderDetailsList.add(details);

            totalPrice += product.getPrice() * quantity;
        }

        // Tạo Order
        Order order = new Order();
        order.setTimeOrder(LocalDateTime.now());
        order.setCustomer(getLoggedCustomer());
        order.setAddress("Địa chỉ mặc định");
        order.setStatusOrder(1);
        order.setTotalPrice(totalPrice);

        // Lưu Order và OrderDetails
        Order savedOrder = orderRepository.save(order);
        orderDetailsService.saveAllOrderDetails(orderDetailsList, savedOrder);

        // Cập nhật số lượng sản phẩm
        for (OrderDetails details : orderDetailsList) {
            Product product = details.getProduct();
            product.setRemainProductQuantity(product.getRemainProductQuantity() - details.getQuantity());
            productService.update(product.getId(), product);
        }
        for (OrderDetails details : orderDetailsList) {
            cartService.removeFromCart(details.getId());
        }
    }
    public List<CartItem> convertOrderDetailsToCartItems(List<OrderDetails> orderDetailsList) {
        List<CartItem> cartItems = new ArrayList<>();
        for (OrderDetails details : orderDetailsList) {
            cartItems.add(new CartItem(details.getProduct(), details.getQuantity()));
        }
        return cartItems;
    }
}

package com.example.casestudy.controller;

import com.example.casestudy.dto.CartItem;
import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    @GetMapping
//    public String checkoutPage(HttpSession session, Model model) {
        // Lấy danh sách sản phẩm từ session
//        List<Product> selectedProducts = (List<Product>) session.getAttribute("selectedProducts");
//        if (selectedProducts == null) {
//            selectedProducts = new ArrayList<>();
//        }

        // Tính tổng giá tiền
//        double totalPrice = selectedProducts.stream()
//                .mapToDouble(product -> product.getDiscountedPrice())
//                .sum();

//

//    }

    // Xử lý thanh toán
//    @PostMapping
//    public String processCheckout(@RequestParam String address, HttpSession session, Model model) {
//        // Lấy danh sách sản phẩm từ session
//        List<Product> selectedProducts = (List<Product>) session.getAttribute("selectedProducts");
//        if (selectedProducts == null || selectedProducts.isEmpty()) {
//            model.addAttribute("message", "Không có sản phẩm nào để thanh toán.");
//            return "checkout/checkout";
//        }
//
//        // Tạo một đơn hàng mới
//        Order order = new Order();
//        order.setTimeOrder(LocalDateTime.now());
//        order.setAddress(address);
//        order.setStatusOrder(1); // 1: Đang xử lý (Processing)
//        order.setTotalPrice(selectedProducts.stream()
//                .mapToDouble(Product::getDiscountedPrice)
//                .sum());
//
//        // Tạo các chi tiết đơn hàng
//        List<OrderDetails> orderDetailsList = new ArrayList<>();
//        for (Product product : selectedProducts) {
//            OrderDetails orderDetails = new OrderDetails();
//            orderDetails.setProduct(product);
//            orderDetails.setQuantity(1); // Số lượng mặc định là 1
//            orderDetails.setPriceDetailOrder(product.getDiscountedPrice());
//            orderDetailsList.add(orderDetails);
//        }
//
//        // Xóa giỏ hàng trong session sau khi thanh toán
//        session.removeAttribute("selectedProducts");
//
//        // Chuyển hướng đến trang xác nhận thanh toán thành công
//        model.addAttribute("order", order);
//        model.addAttribute("orderDetails", orderDetailsList);
//        return "user/success";
//    }

    // Xử lý chuyển sản phẩm từ giỏ hàng sang danh sách thanh toán
    @PostMapping("/add-to-checkout")
    public String addToCheckout(@RequestParam("productId") int[] productIds, HttpSession session) {
        // Giả định session "cart" chứa danh sách tất cả sản phẩm trong giỏ hàng
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Lấy danh sách sản phẩm được chọn
        List<Product> selectedProducts = new ArrayList<>();
        for (int productId : productIds) {
            cart.stream()
                    .filter(product -> product.getId() == productId)
                    .findFirst()
                    .ifPresent(selectedProducts::add);
        }

        // Lưu danh sách sản phẩm được chọn vào session
        session.setAttribute("selectedProducts", selectedProducts);

        return "redirect:/checkout";
    }
}

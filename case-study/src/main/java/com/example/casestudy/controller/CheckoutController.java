package com.example.casestudy.controller;

import com.example.casestudy.dto.CartItem;
import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.CartService;
import com.example.casestudy.service.ProductService;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    // Add selected products to checkout
    @PostMapping("/add-to-checkout")
    public String addToCheckout(@RequestParam(value = "productIds", required = false) List<Integer> productIds,
                                @RequestParam(value = "quantities", required = false) List<Integer> quantities,
                                HttpSession session) {

        if (productIds != null && !productIds.isEmpty() && quantities != null && quantities.size() == productIds.size()) {
            List<CartItem> selectedItems = new ArrayList<>();
            for (int i = 0; i < productIds.size(); i++) {
                Product product = productService.getProductById(productIds.get(i));
                int quantity = quantities.get(i);
                selectedItems.add(new CartItem(product, quantity));  // CartItem contains product and quantity
            }
            session.setAttribute("selectedItems", selectedItems);
        } else {
            // If no product is selected, select all cart items
            List<CartItem> allCartItems = cartService.getAllCartItems();
            session.setAttribute("selectedItems", allCartItems);
        }
        return "redirect:/checkout";
    }

    // Display the checkout page
    @GetMapping
    public String checkoutPage(HttpSession session, Model model) {
        List<CartItem> selectedItems = (List<CartItem>) session.getAttribute("selectedItems");
        if (selectedItems == null || selectedItems.isEmpty()) {
            selectedItems = cartService.getAllCartItems();
        }

        if (selectedItems.isEmpty()) {
            model.addAttribute("message", "Không có sản phẩm nào để thanh toán.");
            return "checkout";
        }

        double totalPrice = selectedItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", selectedItems);
        model.addAttribute("totalPrice", totalPrice);

        return "checkout";
    }

    // Process the checkout (place order)
    @PostMapping
    public String processCheckout(HttpSession session, Model model) {
        List<CartItem> selectedItems = (List<CartItem>) session.getAttribute("selectedItems");
        if (selectedItems == null || selectedItems.isEmpty()) {
            model.addAttribute("message", "Không có sản phẩm nào để thanh toán.");
            return "checkout";
        }

        // Check product availability
        for (CartItem item : selectedItems) {
            Product product = item.getProduct();
            if (product.getRemainProductQuantity() < item.getQuantity()) {
                model.addAttribute("error", "Sản phẩm " + product.getName() + " không đủ số lượng.");
                return "checkout";
            }
        }

        // Process payment here (for now, assume success)
        for (CartItem item : selectedItems) {
            Product product = item.getProduct();
            product.setRemainProductQuantity(product.getRemainProductQuantity() - item.getQuantity());
            productService.updateProduct(product);
        }

        // Remove selected items from the cart
        cartService.removeItems(selectedItems);

        // Clear selected items from the session
        session.removeAttribute("selectedItems");

        return "redirect:/checkout/success";
    }

    @GetMapping("/success")
    public String checkoutSuccess() {
        return "checkout-success";
    }
}

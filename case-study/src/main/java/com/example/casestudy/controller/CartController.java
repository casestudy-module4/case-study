package com.example.casestudy.controller;

import com.example.casestudy.dto.CartItem;
import com.example.casestudy.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("cartTotal", cartService.getCartTotal());
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId) {
        cartService.addToCart(productId);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam Integer orderDetailId, @RequestParam int quantity) {
        cartService.updateCart(orderDetailId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Integer orderDetailId) {
        cartService.removeFromCart(orderDetailId);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String goToCheckout(HttpSession session) {
        List<CartItem> cartItems = cartService.getAllCartItems();
        session.setAttribute("selectedItems", cartItems);
        return "redirect:/checkout";
    }

}
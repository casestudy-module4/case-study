package com.example.casestudy.controller;

import com.example.casestudy.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId) {
        cartService.addToCart(productId);
        return "redirect:/cart";
    }

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("cartTotal", cartService.getCartTotal());
        return "cart/cart";
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

}
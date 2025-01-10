package com.example.casestudy.controller;
import com.example.casestudy.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PayPalController {
    @Autowired
    private IPayService payService;
//    @PostMapping("/checkout/add-to-checkout")
//    public String showCheckoutPage() {
//        return "checkout";
//    }

}
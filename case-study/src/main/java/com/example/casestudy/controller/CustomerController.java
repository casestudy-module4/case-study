package com.example.casestudy.controller;

import com.example.casestudy.dto.CustomerDTO;
import com.example.casestudy.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

    @Controller
    @RequestMapping("/profile")
    public class CustomerController {

        @Autowired
        private ICustomerService customerService;

        @GetMapping
        public String getCustomerProfile(Model model) {
            CustomerDTO customer = customerService.getCustomerById();
            model.addAttribute("customer", customer);
            return "customer";
        }

        @PostMapping
        public String updateCustomerProfile(@ModelAttribute CustomerDTO customer, RedirectAttributes redirectAttributes) {
            customerService.updateCustomer(customer);
            redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công!");
            return "redirect:/profile";
        }
    }
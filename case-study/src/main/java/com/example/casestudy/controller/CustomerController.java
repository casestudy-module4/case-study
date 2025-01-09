package com.example.casestudy.controller;

import com.example.casestudy.model.Customer;
import com.example.casestudy.service.ICustomerServicee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private ICustomerServicee customerService;

    @GetMapping("")
    public String viewCustomers(Model model,
                                @RequestParam(defaultValue = "") String fullName,
                                @RequestParam(defaultValue = "0") int page) {
        model.addAttribute("fullName", fullName);
        model.addAttribute("customers", customerService.findByName(fullName, page));
        return "list";
    }

    @GetMapping("/{id}/edit")
    public String viewUpdateCustomer(@PathVariable int id, Model model) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            model.addAttribute("error", "Customer not found!");
            return "redirect:/customers";
        }
        model.addAttribute("customer", customer);
        return "/customer/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateCustomer(@PathVariable int id,
                                 @Validated @ModelAttribute("customer") Customer customer,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "/customer/edit";
        }
        Customer existingCustomer = customerService.findById(id);
        if (existingCustomer == null) {
            redirectAttributes.addFlashAttribute("error", "Customer not found!");
            return "redirect:/customer/edit";
        }
        customer.setId(id);
        customerService.update(customer);
        redirectAttributes.addFlashAttribute("message", "Customer updated successfully!");
        return "redirect:/customer/edit";
    }
}

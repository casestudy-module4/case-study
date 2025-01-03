package com.example.casestudy.controller;

import com.example.casestudy.model.Customer;
import com.example.casestudy.service.ICustomerService;
import com.example.casestudy.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IOrderService orderService;

    // Chỉ ADMIN có thể truy cập
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/customers")
    public ModelAndView viewAllCustomers(Model model,
                                         @RequestParam(defaultValue = "") String fullName,
                                         @RequestParam(defaultValue = "0") int page) {
        if (page < 0) {
            page = 0;
        }
        model.addAttribute("fullName", fullName);
        return new ModelAndView("customer", "customer", customerService.findByTitle(fullName, page));
    }

    // Chỉ ADMIN có thể truy cập
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/customers/{id}")
    public String viewCustomer(@PathVariable int id, Model model) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with ID: " + id);
        }
        model.addAttribute("selectedCustomer", customer);
        return "modalContent :: customer-details";
    }

    // Chỉ ADMIN có thể thực hiện xóa
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/customers/{id}/delete")
    public String deleteCustomer(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            redirectAttributes.addFlashAttribute("message", "Customer not found");
            redirectAttributes.addFlashAttribute("type", "error");
        } else {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("message", "Customer deleted successfully!");
            redirectAttributes.addFlashAttribute("type", "success");
        }
        return "redirect:/admins/customers";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/customers/advanced-statistics")
    public String getAdvancedCustomerStatistics(Model model) {
        List<Object[]> customerOrderStats = orderService.getCustomerOrderStatistics();
        List<Object[]> customerByAddress = customerService.countCustomersByAddress();
        List<Object[]> averageAgeByGender = customerService.calculateAverageAgeByGender();

        model.addAttribute("customerOrderStats", customerOrderStats);
        model.addAttribute("customerByAddress", customerByAddress);
        model.addAttribute("averageAgeByGender", averageAgeByGender);

        return "advancedCustomerStatistics";
    }
}

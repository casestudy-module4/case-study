package com.example.casestudy.controller;

import com.example.casestudy.dto.OrderHistoryDTO;
import com.example.casestudy.dto.CategoryDTO;
import com.example.casestudy.dto.TopProductDTO;
import com.example.casestudy.model.Banner;
import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.*;
import com.example.casestudy.service.implement.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

//@PreAuthorize("hasAuthority('ROLE_USER')")
@Controller
@RequestMapping("")
public class UserController {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private IProductService productService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IOrderDetailsService orderDetailsService;
    @Autowired
    private IBannerService bannerService;
    @Autowired
    private OrderHistoryService orderHistoryService;
    @Autowired
    private CartService cartService;
    @GetMapping("home")
    public String home(Model model,
                       @RequestParam(defaultValue = "") String name,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "false") String success,
                       Principal principal) {

        Page<Product> productPage = productService.findAll(name.trim(), page);
        List<Product> products = productPage.getContent();
        List<TopProductDTO> bestSellers = productService.getTopSellingOrDefaultProducts();
        List<CategoryDTO> categoryDTOs = categoryService.getAllCategoryDTOs();

        model.addAttribute("categories", categoryDTOs);
        List<List<Product>> productGroups = new ArrayList<>();
        for (int i = 0; i < products.size(); i += 4) {
            int end = Math.min(i + 4, products.size());
            productGroups.add(products.subList(i, end));
        }

        List<Banner> banners = bannerService.findAll();
        model.addAttribute("banners", banners);
        model.addAttribute("bestSellers", bestSellers);
        model.addAttribute("productGroups", productGroups);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("name", name);

        if (principal != null) {
            model.addAttribute("username", principal.getName());
        } else {
            model.addAttribute("username", null);
        }

        if ("true".equals(success)) {
            model.addAttribute("message", "Đăng nhập thành công!");
        }

        return "home";
    }

    // Hiển thị thông tin người dùng
    @GetMapping("/profile")
    public String viewUserProfile(Model model) {
        Customer currentUser = customerService.getCurrentUser();
        model.addAttribute("user", currentUser);
        return "user/profile"; // Trang hiển thị thông tin người dùng
    }

    @PostMapping("profile/update")
    public String updateUserProfile(
            @Validated @ModelAttribute("user") Customer customer,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng kiểm tra lại thông tin!");
            return "redirect:/home/edit"; // Quay lại trang chỉnh sửa nếu có lỗi
        }

        try {
            Customer currentUser = customerService.getCurrentUser();

            currentUser.setFullName(customer.getFullName());
            currentUser.setAddress(customer.getAddress());
            currentUser.setPhoneNumber(customer.getPhoneNumber());
            currentUser.setGender(customer.getGender());
            currentUser.setBirthdate(customer.getBirthdate());
            currentUser.setEmail(customer.getEmail());

            customerService.updateCustomer(currentUser);
            redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật thông tin!");
            return "redirect:/profile/edit"; // Quay lại trang chỉnh sửa nếu lỗi
        }

        return "redirect:/profile"; // Chuyển về trang thông tin người dùng
    }
    /*---- đây la phuong thuc order-history-----*/
    @GetMapping("/order-history")
    public String viewOrderHistory(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerService.findByUsername(userDetails.getUsername());
        List<OrderHistoryDTO> orders = orderHistoryService.getOrderHistory(customer);
        model.addAttribute("orders", orders);
        return "user/order-history";
    }

    @GetMapping("/filter")
    public String filterOrders(@RequestParam Integer statusOrder, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerService.findByUsername(userDetails.getUsername());
        List<OrderHistoryDTO> orders = orderHistoryService.getOrderHistoryByStatus(customer, statusOrder);
        model.addAttribute("orders", orders);
        return "user/order-history";
    }

    @PostMapping("/reorder/{orderId}")
    public String reorder(@PathVariable Integer orderId, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerService.findByUsername(userDetails.getUsername());
        orderHistoryService.reorder(orderId, customer);
        return "redirect:/cart";
    }

    @PostMapping("/review/{orderId}")
    public String addReview(@PathVariable Integer orderId, @RequestParam String review, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerService.findByUsername(userDetails.getUsername());
        orderHistoryService.addReview(orderId, review, customer);
        return "redirect:/user/order-history";
    }
}

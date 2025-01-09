package com.example.casestudy.controller;

import org.springframework.security.core.Authentication;
import com.example.casestudy.dto.CategoryDTO;
import com.example.casestudy.dto.TopProductDTO;
import com.example.casestudy.model.Banner;
import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.*;
import com.example.casestudy.service.implement.AccountService;
import com.example.casestudy.service.implement.EmailService;
import com.example.casestudy.service.implement.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("hasAuthority('ROLE_USER')")
@Controller
@RequestMapping("/home")
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

    @GetMapping("")
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

    @GetMapping("/edit")
    public String editUserProfile(Model model) {
        try {
            Customer currentUser = customerService.getCurrentUser();
            model.addAttribute("user", currentUser);
            model.addAttribute("messege", "messege");
            return "user/edit-profile"; // Trang chỉnh sửa thông tin
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải thông tin người dùng.");
            return "error/404"; // Trang lỗi nếu xảy ra lỗi
        }
    }

    @PostMapping("/update")
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
            return "redirect:/home/edit"; // Quay lại trang chỉnh sửa nếu lỗi
        }

        return "redirect:/home/profile"; // Chuyển về trang thông tin người dùng
    }



}

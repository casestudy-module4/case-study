package com.example.casestudy.controller;

import com.example.casestudy.model.Account;
import com.example.casestudy.model.Customer;
import com.example.casestudy.service.implement.AccountService;
import com.example.casestudy.service.implement.EmailService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Random;

@Controller
public class SecurityController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/custom-login")
    public String loginUser(Model model, @RequestParam(value = "error", defaultValue = "false") String error) {
        model.addAttribute("errorLogin", "true".equals(error) ? "Sai Tên Tài Khoản Hoặc Mật Khẩu." : null);
        model.addAttribute("showModal", "true".equals(error)); // Hiển thị modal khi có lỗi
        return "home";

    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Account()); // Đối tượng để binding form
        return "home"; // Trang HTML cho đăng ký
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Account user,
                               @RequestParam("confirmPassword") String confirmPassword,
                               RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra mật khẩu khớp
            if (!user.getResPassword().equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("registerError", "Mật khẩu không khớp.");
                return "redirect:/home";
            }

            // Đăng ký tài khoản với vai trò mặc định ROLE_USER
            accountService.registerUser(user, "ROLE_USER");

            redirectAttributes.addFlashAttribute("message", "Đăng ký thành công!");
            return "redirect:/home";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/home";
        }
    }

    @GetMapping(value = "/admins/login")
    public String loginPage(Model model, @RequestParam(value = "error", defaultValue = "") String error) {
        if (error != null && error.equals("true")) {
            model.addAttribute("error", "Invalid username or password.");
        } else {
            model.addAttribute("error", null);
        }
        return "loginAdmin";
    }

    @GetMapping(value = "/403")
    public String view403Page() {
        return "403";
    }

    @GetMapping("/admins/forgot-password")
    public String forgotPasswordPage() {
        return "forgotPassword";
    }

    @PostMapping("/admins/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        Account account = accountService.findByEmail(email);
        if (account == null || !account.getRole().equals("ROLE_ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "Email not found or not an admin.");
            return "redirect:/admins/forgot-password";
        }
        accountService.generateOtp(account);
        emailService.sendOtpEmail(email, account.getOtp());

        redirectAttributes.addFlashAttribute("message", "OTP sent to your email.");
        return "redirect:/admins/reset-password?email=" + email;
    }

    @GetMapping("/admins/reset-password")
    public String resetPasswordPage(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "resetPassword";
    }

    @PostMapping("/admins/reset-password")
    public String processResetPassword(@RequestParam("email") String email,
                                       @RequestParam("otp") String otp,
                                       @RequestParam("password") String password,
                                       RedirectAttributes redirectAttributes) {
        try {
            Account account = accountService.findByEmail(email);
            if (account == null) {
                throw new IllegalArgumentException("Account not found.");
            }
            accountService.validateOtp(account, otp);
            accountService.resetPassword(email, password);
            accountService.clearOtp(account);
            redirectAttributes.addFlashAttribute("message", "Password reset successfully!");
            return "redirect:/admins/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admins/reset-password?email=" + email;
        }
    }

    @PostMapping("/admins/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
                return "redirect:/admins/customers";
            }
            accountService.changePassword(username, currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("message", "Password changed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admins/customers";
    }
}

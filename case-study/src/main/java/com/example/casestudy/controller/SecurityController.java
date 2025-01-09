package com.example.casestudy.controller;

import com.example.casestudy.model.Account;
import com.example.casestudy.model.Customer;
import com.example.casestudy.service.implement.AccountService;
import com.example.casestudy.service.implement.EmailService;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/custom-login")
    public String loginUser(HttpServletRequest request, Model model) {
        // Lấy giá trị từ Session
        Object errorLogin = request.getSession().getAttribute("errorLogin");
        Object showModal = request.getSession().getAttribute("showModal");

        // Xóa giá trị khỏi Session sau khi đọc
        request.getSession().removeAttribute("errorLogin");
        request.getSession().removeAttribute("showModal");

        // Gán giá trị vào Model để truyền cho view
        model.addAttribute("errorLogin", errorLogin);
        model.addAttribute("showModal", showModal);

        return "home"; // Trả về trang home
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Account()); // Đối tượng để binding form
        return "home";
    }

//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute("user") Account user,
//                               @RequestParam("confirmPassword") String confirmPassword,
//                               RedirectAttributes redirectAttributes) {
//        try {
//            if (!user.getResPassword().equals(confirmPassword)) {
//                redirectAttributes.addFlashAttribute("registerError", "Mật khẩu không khớp.");
//                return "redirect:/home";
//            }
//            accountService.registerUser(user, "ROLE_USER");
//
//            redirectAttributes.addFlashAttribute("message", "Đăng ký thành công!");
//            return "redirect:/home";
//        } catch (IllegalArgumentException e) {
//            redirectAttributes.addFlashAttribute("error", e.getMessage());
//            return "redirect:/home";
//        }
//    }
@PostMapping("/register")
public String registerUser(@ModelAttribute("user") Account user,
                           @RequestParam("confirmPassword") String confirmPassword,
                           @RequestParam(value = "acceptTerms", defaultValue = "false") boolean acceptTerms,
                           Model model) {
    try {
        // Kiểm tra mật khẩu và xác nhận mật khẩu
        if (!user.getResPassword().equals(confirmPassword)) {
            model.addAttribute("registerError", "Mật khẩu không khớp.");
            model.addAttribute("showRegisterModal", true);
            return "home"; // Giữ nguyên trang hiện tại
        }

        // Kiểm tra điều khoản dịch vụ
        if (!acceptTerms) {
            model.addAttribute("registerError", "Bạn phải đồng ý với Điều khoản dịch vụ.");
            model.addAttribute("showRegisterModal", true);
            return "home";
        }

        // Xử lý đăng ký
        accountService.registerUser(user, "ROLE_USER");

        // Thành công: Hiển thị thông báo thành công
        model.addAttribute("message", "Đăng ký thành công!");
        return "redirect:/home";
    } catch (IllegalArgumentException e) {
        model.addAttribute("registerError", e.getMessage());
        model.addAttribute("showRegisterModal", true);
        return "home";
    }
}
    @PostMapping("/forgot-password")
    public String processUserForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            Account account = accountService.findByEmail(email);
            if (account == null) {
                throw new IllegalArgumentException("Email không tồn tại.");
            }
            accountService.generateOtp(account);
            emailService.sendOtpEmail(email, account.getOtp());

            // Hiển thị thông báo thành công bằng Toast và mở modal Reset Password
            redirectAttributes.addFlashAttribute("message", "OTP đã gửi tới email của bạn.");
            redirectAttributes.addFlashAttribute("showResetModal", true);
        } catch (Exception e) {
            // Hiển thị thông báo lỗi bằng Toast và mở lại modal Forgot Password
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("showForgotModal", true);
        }
        return "redirect:/home";
    }
    @PostMapping("/reset-password")
    public String precessUserResetPassword(@RequestParam("email") String email,
                                           @RequestParam("otp") String otp,
                                           @RequestParam("password") String password,
                                           @RequestParam("confirmPassword") String confirmPassword,
                                           RedirectAttributes redirectAttributes) {
        try {
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Mật khẩu và xác nhận mật khẩu không khớp.");
            }
            Account account = accountService.findByEmail(email);
            if (account == null) {
                throw new IllegalArgumentException("OTP Bị sai");
            }
            accountService.validateOtp(account, otp);
            accountService.resetPassword(email, password);
            accountService.clearOtp(account);

            redirectAttributes.addFlashAttribute("message", "Đặt lại mật khẩu thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("showResetModal", true);
        }
        return "redirect:/home";
    }
}

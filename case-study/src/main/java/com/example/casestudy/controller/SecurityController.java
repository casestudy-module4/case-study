package com.example.casestudy.controller;

import com.example.casestudy.model.Account;
import com.example.casestudy.model.Customer;
import com.example.casestudy.service.implement.AccountService;
import com.example.casestudy.service.implement.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        Object errorLogin = request.getSession().getAttribute("errorLogin");
        Object showModal = request.getSession().getAttribute("showModal");
        request.getSession().removeAttribute("errorLogin");
        request.getSession().removeAttribute("showModal");
        model.addAttribute("errorLogin", errorLogin);
        model.addAttribute("showModal", showModal);
        System.out.println("Controller: errorLogin=" + errorLogin + ", showModal=" + showModal);
        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("/products")) {
            return "test";
        }
        return "home";
    }

//    @GetMapping("/register")
//    public String showRegistrationForm(Model model) {
//        model.addAttribute("user", new Account());
//        return "home";
//    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Account user,
                               @RequestParam("confirmPassword") String confirmPassword,
                               @RequestParam(value = "acceptTerms", defaultValue = "false") boolean acceptTerms,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        try {
            if (!user.getResPassword().equals(confirmPassword)) {
                request.getSession().setAttribute("registerError", "Mật khẩu không khớp.");
                request.getSession().setAttribute("showRegisterModal", true);
                return "redirect:" + request.getHeader("Referer");
            }
            if (!acceptTerms) {
                request.getSession().setAttribute("registerError", "Bạn phải đồng ý với Điều khoản dịch vụ.");
                request.getSession().setAttribute("showRegisterModal", true);
                return "redirect:" + request.getHeader("Referer");
            }
            accountService.registerUser(user, "ROLE_USER");
            redirectAttributes.addFlashAttribute("message", "Đăng ký thành công!");
            return "redirect:/home";
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("registerError", e.getMessage());
            request.getSession().setAttribute("showRegisterModal", true);
            return "redirect:" + request.getHeader("Referer");
        }
    }

    @PostMapping("/forgot-password")
    public String processUserForgotPassword(@RequestParam("email") String email, HttpServletRequest request) {
        try {
            Account account = accountService.findByEmail(email);
            if (account == null) {
                throw new IllegalArgumentException("Email không tồn tại.");
            }
            accountService.generateOtp(account);
            emailService.sendOtpEmail(email, account.getOtp());

            request.getSession().setAttribute("email", email);
            request.getSession().setAttribute("showResetModal", true); // Mở modal đặt lại mật khẩu
        } catch (Exception e) {
            request.getSession().setAttribute("error", e.getMessage());
            request.getSession().setAttribute("showForgotModal", true); // Mở modal quên mật khẩu
        }
        return "redirect:/home"; // Hoặc redirect về trang phù hợp
    }
    @PostMapping("/reset-password")
    public String precessUserResetPassword(@RequestParam("email") String email,
                                           @RequestParam("otp") String otp,
                                           @RequestParam("password") String password,
                                           @RequestParam("confirmPassword") String confirmPassword,
                                           HttpServletRequest request) {
        try {
            if (!password.equals(confirmPassword)) {
                throw new IllegalArgumentException("Mật khẩu và xác nhận mật khẩu không khớp.");
            }
            Account account = accountService.findByEmail(email);
            if (account == null) {
                throw new IllegalArgumentException("OTP không đúng.");
            }
            accountService.validateOtp(account, otp);
            accountService.resetPassword(email, password);
            accountService.clearOtp(account);

            request.getSession().setAttribute("message", "Đặt lại mật khẩu thành công.");
        } catch (Exception e) {
            request.getSession().setAttribute("error", e.getMessage());
            request.getSession().setAttribute("showResetModal", true); // Mở modal đặt lại mật khẩu
        }
        return "redirect:/home"; // Hoặc redirect về trang phù hợp
    }

    @PostMapping("/change-password")
    public String changePasswordUser(@RequestParam("currentPassword") String currentPassword,
                                     @RequestParam("newPassword") String newPassword,
                                     @RequestParam("confirmPassword") String confirmPassword,
                                     RedirectAttributes redirectAttributes) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp!");
                redirectAttributes.addFlashAttribute("showChangePasswordModal", true); // Hiển thị lại modal
                return "redirect:/home";
            }
            accountService.changePassword(username, currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("message", "Thay đổi mật khẩu thành công!");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Current password is incorrect.")) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng.");
            } else {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
            redirectAttributes.addFlashAttribute("showChangePasswordModal", true); // Hiển thị lại modal
        }

        return "redirect:/home";
    }

}

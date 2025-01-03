package com.example.casestudy.controller;

import com.example.casestudy.model.Account;
import com.example.casestudy.service.implement.AccountService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SecurityController {

    @Autowired
    private AccountService accountService;

    @GetMapping(value = "/admins/login")
    public String loginPage(Model model, @RequestParam(value = "error", defaultValue = "") String error) {
        if (error != null && error.equals("true")) {
            model.addAttribute("error", "Invalid username or password.");
        } else {
            model.addAttribute("error", null);
        }
        return "loginAdmin";
    }

//    @GetMapping(value = "/logoutSuccessful")
//    public String logoutSuccessfulPage(Model model) {
//        model.addAttribute("title", "Logout");
//        return "logoutSuccessfulPage";
//    }

    @GetMapping(value = "/403")
    public String view403Page() {
        return "403";
    }

    @GetMapping("/admins/forgot-password")
    public String forgotPasswordPage(Model model) {
        return "forgotPassword";
    }

    @PostMapping("/admins/forgot-password")
    public String processForgotPassword(@RequestParam("username") String username, Model model) {
        // Kiểm tra username có tồn tại và là admin hay không
        Account account = accountService.findByUsername(username);
        if (account == null || !account.getRole().equals("ROLE_ADMIN")) {
            model.addAttribute("error", "Admin username not found.");
            return "forgotPassword";
        }

        // Lưu thông tin và chuyển hướng đến trang đặt lại mật khẩu
        model.addAttribute("username", username);
        return "redirect:/admins/reset-password?username=" + username;
    }

    // Hiển thị form đặt lại mật khẩu
    @GetMapping("/admins/reset-password")
    public String resetPasswordPage(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "resetPassword";
    }

    // Xử lý yêu cầu đặt lại mật khẩu
    @PostMapping("admins/reset-password")
    public String processResetPassword(@RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                       RedirectAttributes redirectAttributes) {
        try {
            boolean success = accountService.resetPassword(username, password);
            if (success) {
                redirectAttributes.addFlashAttribute("message", "Password reset successfully! Please log in.");
                return "redirect:/admins/login";
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to reset password. Please try again.");
                return "redirect:/admins/reset-password?username=" + username;
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admins/reset-password?username=" + username;
        }
    }
}

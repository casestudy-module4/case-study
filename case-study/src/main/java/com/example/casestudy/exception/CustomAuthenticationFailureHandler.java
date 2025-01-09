package com.example.casestudy.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;


public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // Đặt lỗi đăng nhập vào Session
        request.getSession().setAttribute("errorLogin", "Sai Tên Tài Khoản Hoặc Mật Khẩu.");
        request.getSession().setAttribute("showModal", true);

        // Quay lại trang "home"
        response.sendRedirect("/custom-login?error=true");
    }
}
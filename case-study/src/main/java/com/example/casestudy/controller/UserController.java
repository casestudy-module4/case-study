package com.example.casestudy.controller;

import com.example.casestudy.service.*;
import com.example.casestudy.service.implement.AccountService;
import com.example.casestudy.service.implement.EmailService;
import com.example.casestudy.service.implement.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("hasAuthority('ROLE_USER')")
@Controller
@RequestMapping("/users")
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
}

package com.example.casestudy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecurityController {

    @GetMapping(value = "/admins/login")
    public String loginPage(Model model, @RequestParam(value = "error", defaultValue = "") String error) {
        model.addAttribute("error", error);
        return "loginAdmin";
    }

    @GetMapping(value = "/logoutSuccessful")
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "logoutSuccessfulPage";
    }

    @GetMapping(value = "/403")
    public String view403Page() {
        return "403";
    }
}

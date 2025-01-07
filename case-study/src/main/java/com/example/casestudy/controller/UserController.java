package com.example.casestudy.controller;

import com.example.casestudy.dto.TopProductDTO;
import com.example.casestudy.model.Banner;
import com.example.casestudy.service.IBannerService;
import com.example.casestudy.service.IProductService;
import com.example.casestudy.service.IProductServicee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class  UserController {

    @Autowired
    private IProductServicee productService;

    @Autowired
    private IBannerService bannerService;

    @GetMapping("")
    public String home(Model model) {
        List<TopProductDTO> bestSellers = productService.getTopSellingOrDefaultProducts();
        List<Banner> banners = bannerService.findAll();
        model.addAttribute("banners", banners);
        model.addAttribute("bestSellers", bestSellers);
        return "home";
    }
}

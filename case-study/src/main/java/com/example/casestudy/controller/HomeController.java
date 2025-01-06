package com.example.casestudy.controller;

import com.example.casestudy.model.Banner;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.IBannerService;
import com.example.casestudy.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IBannerService bannerService;

    @GetMapping("")
    public String home(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 16);
        Page<Product> productPage = productService.getAllProducts(pageable);
        List<Banner> banners = bannerService.findAll();
        List<Product> bestSellers = productService.getBestSellingProducts();
        model.addAttribute("banners", banners);
        model.addAttribute("bestSellers", bestSellers);
        model.addAttribute("pagedProducts", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "home-use/home";
    }
}
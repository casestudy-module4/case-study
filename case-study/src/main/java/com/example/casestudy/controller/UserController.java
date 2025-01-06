package com.example.casestudy.controller;

import com.example.casestudy.model.Banner;
import com.example.casestudy.model.Category;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.IBannerService;
import com.example.casestudy.service.ICategoryService;
import com.example.casestudy.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class UserController {
    @Autowired
    private IBannerService bannerService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IProductService productService;

    @GetMapping("")
    public String showHomePage(Model model) {
        List<Banner> banners = bannerService.findAll();
        List<Category> categories = categoryService.getAll();
        List<Product> topSellingProducts = productService.getTopSellingProducts();
        model.addAttribute("products", topSellingProducts);
        model.addAttribute("categories", categories);
        model.addAttribute("banners", banners);
        List<Product> products = productService.getAll();
        List<List<Product>> groupedProducts = groupProducts(products, 8);
        model.addAttribute("groupedProducts", groupedProducts);
        return "home";
    }

    private List<List<Product>> groupProducts(List<Product> products, int groupSize) {
        List<List<Product>> groupedProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i += groupSize) {
            groupedProducts.add(products.subList(i, Math.min(i + groupSize, products.size())));
        }
        return groupedProducts;
    }
}

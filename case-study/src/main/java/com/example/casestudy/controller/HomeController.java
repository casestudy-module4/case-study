package com.example.casestudy.controller;

import com.example.casestudy.dto.CategoryDTO;
import com.example.casestudy.dto.TopProductDTO;
import com.example.casestudy.model.Banner;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.IBannerService;
import com.example.casestudy.service.ICategoryService;
import com.example.casestudy.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IBannerService bannerService;

    @GetMapping
    public String home(Model model,
                       @RequestParam(defaultValue = "") String name,
                       @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "false") String success, Principal principal) {

        Page<Product> productPage = productService.findAll(name.trim(), page);
        List<Product> products = productPage.getContent();
        List<TopProductDTO> bestSellers = productService.getTopSellingOrDefaultProducts();

        List<CategoryDTO> categoryDTOs = categoryService.getAllCategoryDTOs();
        model.addAttribute("categories", categoryDTOs);

        List<List<Product>> productGroups = new ArrayList<>();
        for (int i = 0; i < products.size(); i += 4) {
            int end = Math.min(i + 4, products.size());
            productGroups.add(products.subList(i, end));
        }

        List<Banner> banners = bannerService.findAll();
        model.addAttribute("banners", banners);
        model.addAttribute("bestSellers", bestSellers);

        model.addAttribute("productGroups", productGroups);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("name", name);
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        } else {
            model.addAttribute("username", null);
        }
        if ("true".equals(success)) {
            model.addAttribute("message", "Đăng nhập thành công!");
        }
        return "home";
    }
}

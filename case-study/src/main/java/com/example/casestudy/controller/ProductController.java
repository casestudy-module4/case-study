package com.example.casestudy.controller;

import com.example.casestudy.model.Category;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.CategoryService;
import com.example.casestudy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String getProducts(@RequestParam(name = "categoryId", required = false) Long categoryId,
                              @RequestParam(name = "searchQuery", required = false, defaultValue = "") String searchQuery,
                              @RequestParam(defaultValue = "0") int page,
                              Model model) {

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        Page<Product> productPage;

        if (searchQuery != null && !searchQuery.isEmpty()) {
                productPage = productService.searchProductsByName(searchQuery, PageRequest.of(page, 8));
        } else {
            if (categoryId != null) {
                productPage = productService.getProductsByCategory(categoryId, PageRequest.of(page, 8));
            } else {
                productPage = productService.getAllProducts(PageRequest.of(page, 8));
            }
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("searchQuery", searchQuery);

        return "products";
    }
}

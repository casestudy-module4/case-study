package com.example.casestudy.controller;

import com.example.casestudy.dto.CategoryDTO;
import com.example.casestudy.model.Category;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.ICategoryService;
import com.example.casestudy.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/products")
@SessionAttributes("cart")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public String getProducts(@RequestParam(defaultValue = "") String name,
                              @RequestParam(name = "categoryId", required = false) Integer categoryId,
                              @RequestParam(name = "searchQuery", defaultValue = "") String searchQuery,
                              @RequestParam(defaultValue = "0") int page,
                              Model model) {
        List<CategoryDTO> categoryDTOs = categoryService.getAllCategoryDTOs();
        model.addAttribute("categories", categoryDTOs);

        Page<Product> productPage;
        if (!searchQuery.isEmpty()) {
            productPage = productService.findByName(searchQuery, page);
        } else if (categoryId != null) {
            productPage = productService.getProductsByCategory(categoryId, PageRequest.of(page, 9));
        } else {
            productPage = productService.getAllProducts(PageRequest.of(page, 9));
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("searchQuery", searchQuery);

        return "test"; // Tên file HTML hiển thị kết quả
    }

    @GetMapping("/details")
    @ResponseBody
    public Product getProductDetails(@RequestParam Integer productId) {
        return productService.findById(productId);
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId,
                            @RequestParam int quantity,
                            @SessionAttribute("cart") List<Product> cart) {
        Product product = productService.findById(productId);

        for (int i = 0; i < quantity; i++) {
            cart.add(product);
        }
        return "redirect:/cart";
    }
}

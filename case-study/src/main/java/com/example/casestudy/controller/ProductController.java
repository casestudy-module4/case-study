package com.example.casestudy.controller;

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

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public String getProducts(@RequestParam(name = "categoryId", required = false) Integer categoryId,
                              @RequestParam(name = "searchQuery", required = false, defaultValue = "") String searchQuery,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "") String name,
                              Model model) {

        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);

        Page<Product> productPage;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            productPage = productService.searchProductsByName(searchQuery, PageRequest.of(page, 8));
        } else {
            if (categoryId != null) {
                productPage = productService.getProductsByCategory(categoryId, PageRequest.of(page, 8));
            } else {
                if(page < 0){
                    page = 0;
                }
                Page<Product> products = productService.findAll(name, page);
                model.addAttribute("products", products);

                model.addAttribute("categories", categoryService.getAll());
            }
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("searchQuery", searchQuery);

        return "products";
    }

    @GetMapping("/details")
    @ResponseBody
    public Product getProductDetails(@RequestParam Integer productId) {
        return productService.findById(productId);
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId, @RequestParam int quantity, @SessionAttribute("cart") List<Product> cart) {
        Product product = productService.findById(productId);

        for (int i = 0; i < quantity; i++) {
            cart.add(product);
        }

        return "redirect:/cart";
    }
}

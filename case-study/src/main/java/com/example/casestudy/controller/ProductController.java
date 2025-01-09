package com.example.casestudy.controller;

import com.example.casestudy.dto.CategoryDTO;
import com.example.casestudy.model.Category;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.ICategoryService;
import com.example.casestudy.service.IProductService;
import jakarta.servlet.http.HttpServletRequest;
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
                              Model model, HttpServletRequest request) {
        // Lấy danh sách danh mục và thêm vào model
        List<CategoryDTO> categoryDTOs = categoryService.getAllCategoryDTOs();
        model.addAttribute("categories", categoryDTOs);

        // Lấy danh sách sản phẩm dựa trên điều kiện tìm kiếm hoặc danh mục
        Page<Product> productPage;
        if (!searchQuery.isEmpty()) {
            productPage = productService.findByName(searchQuery, page);
        } else if (categoryId != null) {
            productPage = productService.getProductsByCategory(categoryId, PageRequest.of(page, 9));
        } else {
            productPage = productService.getAllProducts(PageRequest.of(page, 9));
        }

        // Truyền các thuộc tính cần thiết vào model
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("searchQuery", searchQuery);

        // Thêm thuộc tính liên quan đến lỗi đăng nhập
        Object errorLogin = request.getSession().getAttribute("errorLogin");
        Object showModal = request.getSession().getAttribute("showModal");

        request.getSession().removeAttribute("errorLogin");
        request.getSession().removeAttribute("showModal");

        model.addAttribute("errorLogin", errorLogin);
        model.addAttribute("showModal", showModal);

        addRegisterAttributes(request, model);
        addPasswordResetAttributes(request, model);

        return "test"; // Trả về tên view
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

    private void addRegisterAttributes(HttpServletRequest request, Model model) {
        Object registerError = request.getSession().getAttribute("registerError");
        Object showRegisterModal = request.getSession().getAttribute("showRegisterModal");

        request.getSession().removeAttribute("registerError");
        request.getSession().removeAttribute("showRegisterModal");

        model.addAttribute("registerError", registerError);
        model.addAttribute("showRegisterModal", showRegisterModal);
    }

    private void addPasswordResetAttributes(HttpServletRequest request, Model model) {
        Object error = request.getSession().getAttribute("error");
        Object email = request.getSession().getAttribute("email");
        Object showForgotModal = request.getSession().getAttribute("showForgotModal");
        Object showResetModal = request.getSession().getAttribute("showResetModal");

        request.getSession().removeAttribute("error");
        request.getSession().removeAttribute("email");
        request.getSession().removeAttribute("showForgotModal");
        request.getSession().removeAttribute("showResetModal");

        model.addAttribute("error", error);
        model.addAttribute("email", email);
        model.addAttribute("showForgotModal", showForgotModal);
        model.addAttribute("showResetModal", showResetModal);
    }
}

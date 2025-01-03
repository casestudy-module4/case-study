package com.example.casestudy.controller;

import com.example.casestudy.model.Product;
import com.example.casestudy.service.ICategoryService;
import com.example.casestudy.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/home")
public class ProductController {

    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;
    @GetMapping
    public String showHomePage(@RequestParam(defaultValue = "") String name, Model model) {
        Page<Product> products = productService.findAll(name, 0);
        model.addAttribute("products", products);
        return "home";
    }
    @GetMapping("/show-add")
        public String showAddProductForm(Model model) {
        model.addAttribute("products", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "product/create";
    }
    @PostMapping("/create")
    public String createProduct( @ModelAttribute("products") Product product,
                                 @RequestParam("image1") MultipartFile image,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            model.addAttribute("categories", product.getCategory());
            return "home";
        }
        String uploadDir = "D:\\JAVA FULL STACK\\Module4\\case_study\\case-study\\case-study\\src\\main\\resources\\static\\images";
        File uploadDirPath = new File(uploadDir);
        if (!uploadDirPath.exists()) {
            uploadDirPath.mkdirs();
        }
        String fileName = image.getOriginalFilename();
        File uploadFile = new File(uploadDirPath, fileName);

        try {
            image.transferTo(uploadFile);
            product.setImage("/images/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error while uploading image");
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }

        if(product.getId()==null || productService.findById(product.getId()) == null) {
            //tinh toán sl còn lại
            product.setRemainProductQuantity(12);
            productService.addNew(product);
        }else {
            throw new DuplicateKeyException("Product already exists");
        }
        redirectAttributes.addFlashAttribute("message", "Product created successfully");
        return "redirect:/home";
    }

    @PostMapping("/{id}/update")
    public String updateProduct(@PathVariable int id, @ModelAttribute Product product) {
        productService.update(id, product);
        return "redirect:/home";
    }

    @GetMapping("/{id}/delete")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteById(id);
        return "redirect:/home";
    }
}

package com.example.casestudy.controller;

import com.example.casestudy.model.Category;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/home/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;
    @GetMapping
    public String category(@RequestParam(defaultValue = "") String name, Model model) {
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return "category/list";
    }
    @GetMapping("/show-add")
    public String showAdd(Model model) {
        model.addAttribute("categories", new Category());
        return "category/add-category";
    }
    @PostMapping("/add")
    public String createCategory(@Validated @ModelAttribute ("categories") Category category,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes, Model model) {
        String id = (String) model.getAttribute("name");
        if(bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            return "home/category";
        }
        if(category.getId()==null || categoryService.findById(category.getId()) == null) {
            categoryService.addNew(category);
        }else {
            throw new DuplicateKeyException("Category already exists");
        }
        redirectAttributes.addFlashAttribute("message", "Category created successfully");
        return "redirect:/home/category";
    }
    @PostMapping("/{id}/update")
    public String updateProduct(@PathVariable int id, @ModelAttribute Category category) {
        categoryService.update(id, category);
        return "redirect:/home/category";
    }
    @GetMapping("/{id}/delete")
    public String deleteProduct(@PathVariable int id, RedirectAttributes redirectAttributes) {
        if (categoryService.findById(id) == null) {
            redirectAttributes.addFlashAttribute("error", "Category doesn't exist");
            return "redirect:/home/category";
        }
        categoryService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Delete category successfully");
        return "redirect:/home/category";
    }
}

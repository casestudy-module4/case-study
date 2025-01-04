package com.example.casestudy.controller;

import com.example.casestudy.model.Category;
import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.ICategoryService;
import com.example.casestudy.service.ICustomerService;
import com.example.casestudy.service.IOrderService;
import com.example.casestudy.service.IProductService;
import com.example.casestudy.service.implement.AccountService;
import com.example.casestudy.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private IProductService productService;
    @Autowired
    private ICategoryService categoryService;
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/home")
    public String showHomePage(@RequestParam(defaultValue = "") String name, Model model) {
        Page<Product> products = productService.findAll(name, 0);
        model.addAttribute("products", products);

        model.addAttribute("categories", categoryService.getAll());

        List<Category> categoryList = categoryService.getAll();
        for(Category category: categoryList){
            System.out.println("category.getId()category.getId()category.getId()" + category.getId());
        }
        return "product/home";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/home/create")
    public String createProduct( @ModelAttribute("products") Product product,
                                 @RequestParam("image1") MultipartFile image,
                                 @RequestParam("category.id") Integer categoryId,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            model.addAttribute("categories", categoryService.getAll());

            return "product/home";
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
            return "product/home";
        }
        if(product.getId()==null || productService.findById(product.getId()) == null) {
            //tinh toán sl còn lại
            product.setRemainProductQuantity(12);
            productService.addNew(product);
        }else {
            throw new DuplicateKeyException("Product already exists");
        }
        redirectAttributes.addFlashAttribute("message", "Product created successfully");
        return "redirect:/admins/home";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/home/{id}/update")
    public String updateProduct( @PathVariable int id,
                                @ModelAttribute("products") Product product,
                                 @RequestParam("images") MultipartFile image,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            model.addAttribute("categories", product.getCategory());
            return "product/home";
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
            return "product/fragment";
        }

        if (product.getId() != null || productService.findById(product.getId()) != null) {
            //tinh toán sl còn lại
            product.setRemainProductQuantity(12);
            productService.update(id, product);
        } else {
            throw new DuplicateKeyException("Product already exists");
        }
        redirectAttributes.addFlashAttribute("message", "Product updated successfully");
        return "redirect:/admins/home";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/home/{id}/delete")
    public String deleteProduct(@PathVariable int id, RedirectAttributes redirectAttributes) {
        if(productService.findById(id) == null) {
            redirectAttributes.addFlashAttribute("message", "Product not found");
            return "redirect:/admins/home";
        }
        productService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Product deleted successfully");
        return "redirect:/admins/home";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/home/{id}")
    public String detailProduct(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.findById(id));
//        model.addAttribute("categories", categoryService.getAll());
        return "product/fragment";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/category")
    public String category(@RequestParam(defaultValue = "") String name, Model model) {
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return "category/list";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/category/add")
    public String createCategory(@Validated @ModelAttribute("category") Category category,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "category/list";
        }
        if (category.getId() != null) {
            if (categoryService.findById(category.getId()) != null) {
                throw new DuplicateKeyException("Category already exists");
            }
        } else {
            categoryService.addNew(category);
        }
        redirectAttributes.addFlashAttribute("message", "Category created successfully");
        return "redirect:/admins/category";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/category/{id}/update")
    public String updateProduct(@PathVariable int id, @ModelAttribute Category category) {
        categoryService.update(id, category);
        return "redirect:/admins/category";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/category/{id}/delete")
    public String deleteCategory(@PathVariable int id, RedirectAttributes redirectAttributes) {
        if (categoryService.findById(id) == null) {
            redirectAttributes.addFlashAttribute("error", "Category doesn't exist");
            return "redirect:/admins/category";
        }
        categoryService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Delete category successfully");
        return "redirect:/admins/category";
    }
    // Chỉ ADMIN có thể truy cập
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/customers")
    public ModelAndView viewAllCustomers(Model model,
                                         @RequestParam(defaultValue = "") String fullName,
                                         @RequestParam(defaultValue = "0") int page) {
        if (page < 0) {
            page = 0;
        }
        model.addAttribute("fullName", fullName);
        return new ModelAndView("customer", "customer", customerService.findByTitle(fullName, page));
    }

    // Chỉ ADMIN có thể truy cập
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/customers/{id}")
    public String viewCustomer(@PathVariable int id, Model model) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with ID: " + id);
        }
        model.addAttribute("selectedCustomer", customer);
        return "modalContent :: customer-details";
    }

    // Chỉ ADMIN có thể thực hiện xóa
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/customers/{id}/delete")
    public String deleteCustomer(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Customer customer = customerService.findById(id);
        if (customer == null) {
            redirectAttributes.addFlashAttribute("message", "Customer not found");
            redirectAttributes.addFlashAttribute("type", "error");
        } else {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("message", "Customer deleted successfully!");
            redirectAttributes.addFlashAttribute("type", "success");
        }
        return "redirect:/admins/customers";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/statistics")
    public String getAdvancedCustomerStatistics(Model model) {
        model.addAttribute("mostOrders", orderService.getCustomerWithMostOrders());
        model.addAttribute("highestSpender", orderService.getCustomerWithHighestSpending());
        model.addAttribute("popularProduct", productService.getMostPurchasedProduct());
        model.addAttribute("salesData", productService.getSalesByMonth());
        model.addAttribute("accountRegistrationData", accountService.getAccountRegistrationsByMonth());
        return "statistic";
    }
}

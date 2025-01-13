package com.example.casestudy.controller;

import com.example.casestudy.dto.PaymentRequest;
import com.example.casestudy.model.Category;
import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.*;
import com.example.casestudy.service.implement.AccountService;
import com.example.casestudy.service.implement.EmailService;
import com.example.casestudy.service.implement.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Controller
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private IProductService productService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IOrderDetailsService orderDetailsService;
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/home")
    public String showHomePage(@RequestParam(defaultValue = "") String name,
                               Model model,
                               @RequestParam(defaultValue = "0") int page) {
        if (page < 0) page = 0;

        Page<Product> products = productService.findByName(name, page);
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAll());
        return "product/home";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/home/create")
    public String createProduct(@Validated @ModelAttribute("product") Product product,
                                @RequestParam("image1") MultipartFile image,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (image != null && !image.isEmpty()) {
            System.out.println("File received: " + image.getOriginalFilename());
        } else {
            System.out.println("No file received");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "product/home";
        }
        // Xử lý upload file ảnh
        if (!image.isEmpty()) {
            String uploadDir = "D:\\JAVA FULL STACK\\Module4\\case_study\\case-study\\case-study\\src\\main\\resources\\static\\images";
            String fileName = image.getOriginalFilename();
            File uploadFile = new File(uploadDir, fileName);
            try {
                image.transferTo(uploadFile);
                product.setImage("/images/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Failed to upload image.");
                return "product/home";
            }
        } else {
            product.setImage(null);
        }
        try {
            product.setImage("/images/" + image.getOriginalFilename());
            if (product.getRemainProductQuantity() == null) {
                product.setRemainProductQuantity(product.getTotalProductQuantity());
            }
            product.setRemainProductQuantity(product.getTotalProductQuantity());
            productService.addNew(product);
            redirectAttributes.addFlashAttribute("message", "Thêm sản phẩm thành công!");
        } catch (Exception e) {
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("error", "Lỗi khi tải ảnh: " + e.getMessage());
            return "product/home";
        }
        return "redirect:/admins/home";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/home/{id}/update")
    public String updateProduct(@PathVariable int id,
                                @ModelAttribute("product") Product product,
                                @RequestParam("images") MultipartFile image,
                                BindingResult bindingResult,
                                @Validated
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (bindingResult.hasErrors()) {
            if (image == null || image.isEmpty()) {
                bindingResult.rejectValue("image", "NotNull", "Please update image");
            }
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "product/home";
        }
        try {
            if (!image.isEmpty()) {
                String imagePath = saveImage(image);
                product.setImage(imagePath);
            }
            productService.update(id, product);
            redirectAttributes.addFlashAttribute("message", "Cập nhật sản phẩm thành công!");
        } catch (IOException e) {
            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("error", "Lỗi khi tải ảnh: " + e.getMessage());
            return "product/home";
        }
        return "redirect:/admins/home";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/home/{id}/delete")
    public String deleteProduct(@PathVariable int id, RedirectAttributes redirectAttributes) {
        if (productService.findById(id) == null) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại");
        } else {
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công");
        }
        return "redirect:/admins/home";
    }

    private String saveImage(MultipartFile image) throws IOException {
        String uploadDir = "D:\\JAVA FULL STACK\\Module4\\case_study\\case-study\\case-study\\src\\main\\resources\\static\\images";
        File uploadDirPath = new File(uploadDir);

        if (!uploadDirPath.exists()) {
            uploadDirPath.mkdirs();
        }

        if (image.isEmpty() || !image.getContentType().startsWith("image/")) {
            throw new IOException("File không hợp lệ hoặc không phải hình ảnh");
        }

        String fileName = image.getOriginalFilename();
        File uploadFile = new File(uploadDirPath, fileName);

        try {
            image.transferTo(uploadFile);
            System.out.println("Image file received: " + image.getOriginalFilename());
            return "/images/" + fileName;
        } catch (IOException e) {
            throw new IOException("Lỗi khi lưu file", e);  // Ném ngoại lệ kèm thông tin chi tiết
        }
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/category")
    public String category(@RequestParam(defaultValue = "") String name, Model model, @RequestParam(defaultValue = "0") int page) {
        if (page < 0) {
            page = 0;
        }
        Page<Category> categories = categoryService.findByName(name, page);
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
    public String updateProduct(@PathVariable int id, @ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        categoryService.update(id, category);
        redirectAttributes.addFlashAttribute("message", "Updated category successfully");
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

package com.example.casestudy.controller;

import com.example.casestudy.dto.CategoryDTO;
import com.example.casestudy.dto.CartItem;
import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.model.Product;
import com.example.casestudy.repository.*;
import com.example.casestudy.dto.OrderHistoryDTO;
import com.example.casestudy.dto.CategoryDTO;
import com.example.casestudy.dto.TopProductDTO;
import com.example.casestudy.model.Banner;
import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.*;
import com.example.casestudy.service.implement.AccountService;

import com.example.casestudy.service.implement.EmailService;
import com.example.casestudy.service.implement.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.example.casestudy.service.implement.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import java.security.Principal;
import java.util.List;

//@PreAuthorize("hasAuthority('ROLE_USER')")
@Controller
@RequestMapping("")
public class UserController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private CustomerRepository customerRepository;
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
    @Autowired
    private IBannerService bannerService;
    @Autowired
    private OrderHistoryService orderHistoryService;
    @Autowired
    private CartService cartService;
    @GetMapping("home")
    public String home(Model model,
                       @RequestParam(defaultValue = "") String name,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "false") String success,
                       Principal principal) {

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

    // Hiển thị thông tin người dùng
    @GetMapping("/profile")
    public String viewUserProfile(Model model) {
        Customer currentUser = customerService.getCurrentUser();
        model.addAttribute("user", currentUser);
        return "user/profile"; // Trang hiển thị thông tin người dùng
    }

    @PostMapping("profile/update")
    public String updateUserProfile(
            @Validated @ModelAttribute("user") Customer customer,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng kiểm tra lại thông tin!");
            return "redirect:/home/edit"; // Quay lại trang chỉnh sửa nếu có lỗi
        }

        try {
            Customer currentUser = customerService.getCurrentUser();

            currentUser.setFullName(customer.getFullName());
            currentUser.setAddress(customer.getAddress());
            currentUser.setPhoneNumber(customer.getPhoneNumber());
            currentUser.setGender(customer.getGender());
            currentUser.setBirthdate(customer.getBirthdate());
            currentUser.setEmail(customer.getEmail());

            customerService.updateCustomer(currentUser);
            redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật thông tin!");
            return "redirect:/profile/edit"; // Quay lại trang chỉnh sửa nếu lỗi
        }

        return "redirect:/profile"; // Chuyển về trang thông tin người dùng
    }
    /*---- đây la phuong thuc order-history-----*/
    @GetMapping("/order-history")
    public String viewOrderHistory(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerService.findByUsername(userDetails.getUsername());
        List<OrderHistoryDTO> orders = orderHistoryService.getOrderHistory(customer);
        model.addAttribute("orders", orders);
        return "user/order-history";
    }

    @GetMapping("/filter")
    public String filterOrders(@RequestParam Integer statusOrder, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerService.findByUsername(userDetails.getUsername());
        List<OrderHistoryDTO> orders = orderHistoryService.getOrderHistoryByStatus(customer, statusOrder);
        model.addAttribute("orders", orders);
        return "user/order-history";
    }

    @PostMapping("/reorder/{orderId}")
    public String reorder(@PathVariable Integer orderId, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerService.findByUsername(userDetails.getUsername());
        orderHistoryService.reorder(orderId, customer);
        return "redirect:/cart";
    }

    @PostMapping("/review/{orderId}")
    public String addReview(@PathVariable Integer orderId, @RequestParam String review, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerService.findByUsername(userDetails.getUsername());
        orderHistoryService.addReview(orderId, review, customer);
        return "redirect:/user/order-history";
    }

    @GetMapping("/products")
    public String getProducts(@RequestParam(defaultValue = "") String name,
                              @RequestParam(name = "categoryId", required = false) Integer categoryId,
                              @RequestParam(name = "searchQuery", defaultValue = "") String searchQuery,
                              @RequestParam(name = "orderby", defaultValue = "default") String orderby,
                              @RequestParam(defaultValue = "0") int page,
                              Model model,
                              @RequestParam(defaultValue = "false") String success, Principal principal, HttpServletRequest request) {
        List<CategoryDTO> categoryDTOs = categoryService.getAllCategoryDTOs();
        model.addAttribute("categories", categoryDTOs);

        Page<Product> productPage;
        if (!searchQuery.isEmpty()) {
            productPage = productService.findByName(searchQuery, page);
        } else if (categoryId != null) {
            productPage = productService.getProductsByCategory(categoryId, PageRequest.of(page, 9));
        } else {
            switch (orderby) {
                case "price":
                    productPage = productService.getAllProducts(PageRequest.of(page, 9, Sort.by("price").ascending()));
                    break;
                case "price-desc":
                    productPage = productService.getAllProducts(PageRequest.of(page, 9, Sort.by("price").descending()));
                    break;
                default:
                    productPage = productService.getAllProducts(PageRequest.of(page, 9));
            }
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("orderby", orderby);

        if (principal != null) {
            model.addAttribute("username", principal.getName());
        } else {
            model.addAttribute("username", null);
        }
        if ("true".equals(success)) {
            model.addAttribute("message", "Đăng nhập thành công!");
        }
        Object errorLogin = request.getSession().getAttribute("errorLogin");
        Object showModal = request.getSession().getAttribute("showModal");

        // Xóa giá trị sau khi lấy
        request.getSession().removeAttribute("errorLogin");
        request.getSession().removeAttribute("showModal");

        // Truyền vào model
        model.addAttribute("errorLogin", errorLogin);
        model.addAttribute("showModal", showModal);
        addRegisterAttributes(request, model);
        addPasswordResetAttributes(request, model);

        return "products";
    }


    @GetMapping("/products/details")
    @ResponseBody
    public Product getProductDetails(@RequestParam Integer productId) {
        return productService.findById(productId);
    }

    @PostMapping("/cart/add")
    public @ResponseBody
    UserController.Response addToCart(@RequestBody CartItem cartItemDTO, HttpSession session) {
        try {

            // Kiểm tra nếu có session với đơn hàng (order)

            Order order = (Order) session.getAttribute("order");
            if (order == null) {
                order = new Order();  // Nếu chưa có order, tạo một order mới
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                Customer customer = accountService.findByUsername(username).getCustomer();
                order.setCustomer(customer);
                order.setStatusOrder(0);
                order.setTimeOrder(null);
                order.setTotalPrice(0.0);
                order = orderRepository.save(order);  // Lưu order vào DB
                session.setAttribute("order", order);  // Lưu vào session
            }

            // Lấy sản phẩm từ service
            Product product = productService.getProductById(cartItemDTO.getProduct().getId());
            if (product == null) {
                return new UserController.Response(false, "Sản phẩm không tồn tại");
            }



            // Kiểm tra xem sản phẩm có trong giỏ hàng chưa, nếu chưa thì tạo mới OrderDetails
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setProduct(product);
            orderDetails.setQuantity(cartItemDTO.getQuantity());
            orderDetails.setPriceDetailOrder(product.getPrice());
            orderDetails.setOrder(order);

            // Lưu vào cơ sở dữ liệu
            orderDetailsRepository.save(orderDetails);

            return new UserController.Response(true, "Sản phẩm đã được thêm vào giỏ hàng");
        } catch (Exception e) {
            e.printStackTrace();  // In ra thông báo lỗi để kiểm tra
            return new UserController.Response(false, "Đã có lỗi xảy ra");
        }
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
       String username = SecurityContextHolder.getContext().getAuthentication().getName();
       Customer customer = accountService.findByUsername(username).getCustomer();

        // Lấy danh sách chi tiết đơn hàng (orderDetails) từ cơ sở dữ liệu
        List<OrderDetails> cartItems = orderDetailsRepository.findAllByOrderCustomerId(customer.getId());

        // Tính tổng giá trị giỏ hàng
        double cartTotal = cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceDetailOrder())
                .sum();

        // Gửi dữ liệu vào model để hiển thị trên trang giỏ hàng
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartTotal);

        return "cart";
    }


    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Integer orderDetailId, @RequestParam int quantity) {
        cartService.updateCart(orderDetailId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Integer orderDetailId) {
        cartService.removeFromCart(orderDetailId);
        return "redirect:/cart";
    }

    @GetMapping("/cart/checkout")
    public String checkoutPage(Model model) {
        Integer customerId = 1; // Gán customerId mặc định là 1

        // Kiểm tra khách hàng hợp lệ
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

//        // Lưu giỏ hàng thành đơn hàng
//        cartService.saveCartToOrder(customerId);

        // Lấy tất cả thông tin đơn hàng của khách hàng từ cơ sở dữ liệu
        List<OrderDetails> orderDetailsList = orderDetailsRepository.findAllByOrderCustomerId(customerId);

        // Tính tổng giá trị đơn hàng
        double orderTotal = orderDetailsList.stream()
                .mapToDouble(detail -> detail.getQuantity() * detail.getPriceDetailOrder())
                .sum();

        // Thêm thông tin vào model để hiển thị trong view
        model.addAttribute("orderDetails", orderDetailsList);
        model.addAttribute("orderTotal", orderTotal);
        model.addAttribute("customerId", customerId);

        return "test1"; // Tên view là checkout.html
    }

    public class Response {
        private boolean success;
        private String message;

        public Response(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @PostMapping("/cart/select")
    @ResponseBody
    public double calculateSelectedTotal(@RequestParam List<Integer> selectedIds) {
        // Lấy danh sách sản phẩm từ cơ sở dữ liệu dựa trên ID được chọn
        List<OrderDetails> selectedItems = orderDetailsRepository.findAllById(selectedIds);

        // Tính tổng giá trị các sản phẩm được chọn
        return selectedItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceDetailOrder())
                .sum();
    }

    @GetMapping("/introduction")
    public String introduction(Model model,
                               @RequestParam(defaultValue = "false") String success, Principal principal, HttpServletRequest request) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        } else {
            model.addAttribute("username", null);
        }
        if ("true".equals(success)) {
            model.addAttribute("message", "Đăng nhập thành công!");
        }
        Object errorLogin = request.getSession().getAttribute("errorLogin");
        Object showModal = request.getSession().getAttribute("showModal");

        // Xóa giá trị sau khi lấy
        request.getSession().removeAttribute("errorLogin");
        request.getSession().removeAttribute("showModal");

        // Truyền vào model
        model.addAttribute("errorLogin", errorLogin);
        model.addAttribute("showModal", showModal);
        addRegisterAttributes(request, model);
        addPasswordResetAttributes(request, model);
        return "introduction";
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

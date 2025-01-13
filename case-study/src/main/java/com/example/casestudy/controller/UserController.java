package com.example.casestudy.controller;
import com.example.casestudy.dto.CategoryDTO;
import com.example.casestudy.dto.CartItem;
import com.example.casestudy.dto.PaymentRequest;
import com.example.casestudy.model.*;
import com.example.casestudy.repository.*;
import com.example.casestudy.dto.OrderHistoryDTO;
import com.example.casestudy.dto.CategoryDTO;
import com.example.casestudy.dto.TopProductDTO;
import com.example.casestudy.model.Banner;
import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Product;
import com.example.casestudy.service.*;
import com.example.casestudy.service.implement.AccountService;

import com.example.casestudy.service.implement.CartService;
import com.example.casestudy.service.implement.EmailService;
import com.example.casestudy.service.implement.PaymentService;
import com.paypal.api.payments.Links;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import com.paypal.api.payments.Payment;
import com.example.casestudy.service.implement.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.casestudy.model.Payment.PaymentMethod.PAYPAL;
import static com.example.casestudy.model.Payment.PaymentStatus.COMPLETED;

@Controller
@RequestMapping
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
    private IProductService productService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private CartService cartService;
    @Autowired
    private IPayService payService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IOrderDetailsService orderDetailsService;
    private static final String EXCHANGE_RATE_API = "https://open.er-api.com/v6/latest/USD";
    private static final String SUCCESS_URL = "http://localhost:8080/success";
    private static final String CANCEL_URL = "http://localhost:8080/cancel";
    @Autowired
    private IBannerService bannerService;
    private JavaMailSenderImpl mailSender;
    @Autowired
    private OrderHistoryService orderHistoryService;
    private PaymentService paymentService;

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
    private PaymentRepository paymentRepository;
    @Autowired
    private HttpSession httpSession;


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
            Order order = (Order) session.getAttribute("order");
            if (order == null) {
                order = new Order();
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                Customer customer = accountService.findByUsername(username).getCustomer();
                order.setCustomer(customer);
                order.setStatusOrder(0);
                order.setTimeOrder(null);
                order.setTotalPrice(0.0);
                order = orderRepository.save(order);
                session.setAttribute("order", order);
            }
            Product product = productService.getProductById(cartItemDTO.getProduct().getId());
            if (product == null) {
                return new UserController.Response(false, "Sản phẩm không tồn tại");
            }
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setProduct(product);
            orderDetails.setQuantity(cartItemDTO.getQuantity());
            orderDetails.setPriceDetailOrder(product.getPrice());
            orderDetails.setOrder(order);
            orderDetails.setPriceDetailOrder(product.getPrice());

            orderDetailsRepository.save(orderDetails);

            return new UserController.Response(true, "Sản phẩm đã được thêm vào giỏ hàng");
        } catch (Exception e) {
            e.printStackTrace();
            return new UserController.Response(false, "Đã có lỗi xảy ra");
        }
    }
        @GetMapping("/cart")
        public String showCart (Model model, HttpSession session) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Customer customer = accountService.findByUsername(username).getCustomer();
            //lay  order từ session kểm tra tồn tại trong payment DB
            Order order = (Order) session.getAttribute("order");
            paymentRepository.findByOrderId(order.getId());
            if(paymentRepository.findByOrderId(order.getId()) == null){
                List<OrderDetails> cartItems = orderDetailsRepository.findAllByOrderCustomerId(customer.getId());
                double cartTotal = cartService.getCartTotal();
                model.addAttribute("cartItems", cartItems);
                model.addAttribute("cartTotal", cartTotal);
            } else {
                model.addAttribute("cartItems", null);
                model.addAttribute("cartTotal", null);
            }
            return "cart";
        }
        @PostMapping("/cart/update")
        public String updateCart(@RequestParam Integer orderDetailId,@RequestParam int quantity){
            cartService.updateCart(orderDetailId, quantity);
            return "redirect:/cart";
        }

        @PostMapping("/cart/remove")
        public String removeFromCart (@RequestParam Integer orderDetailId){
            cartService.removeFromCart(orderDetailId);
            return "redirect:/cart";
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
    @GetMapping("/cart/checkout")
    public String checkoutPage(Model model, Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.findByUsername(username);
            if (account == null) {
                throw new RuntimeException("Không tìm thấy tài khoản của người dùng");
            }
            Customer customer = account.getCustomer();
            model.addAttribute("customer", customer);
            model.addAttribute("customerId", customer.getId());
            model.addAttribute("customerName", customer.getFullName());
            model.addAttribute("customerPhoneNumber", customer.getPhoneNumber());
            model.addAttribute("customerEmail", customer.getEmail());
            model.addAttribute("customerAddress", customer.getAddress());
            List<OrderDetails> orderDetailsList = orderDetailsRepository.findAllByOrderCustomerId(customer.getId());
            double orderTotal = orderDetailsList.stream()
                    .mapToDouble(detail -> detail.getQuantity() * detail.getPriceDetailOrder())
                    .sum();
            model.addAttribute("orderDetails", orderDetailsList);
            model.addAttribute("orderTotal", orderTotal);
                return "payment/checkout";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Đã có lỗi xảy ra khi tải trang thanh toán");
            return "error";
        }
    }
    @PostMapping("/checkout/process-payment")
    public String processPayment( @RequestParam("total") Double total, HttpSession session){
        try{
            session.setAttribute("totalAmount", total);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(EXCHANGE_RATE_API, String.class);
            System.out.println("Response từ API: " + response);
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("rates")) {
                JSONObject rates = jsonResponse.getJSONObject("rates");
                if (rates.has("VND")) {
                    double exchangeRate = rates.getDouble("VND");
                    double convertedAmount = total / exchangeRate;
                    Payment payment = payService.createPaymentWithPaypal(
                            convertedAmount,
                            "USD",
                            "paypal",
                            "sale",
                            CANCEL_URL,
                            SUCCESS_URL);

                    for (Links link : payment.getLinks()) {
                        if (link.getRel().equals("approval_url")) {
                            return "redirect:" + link.getHref();
                        }
                    }
                } else {
                    throw new RuntimeException("Tỷ giá VND không tồn tại trong phản hồi API.");
                }
            } else {
                throw new RuntimeException("Phản hồi từ API không chứa trường 'rates'.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Đã xảy ra lỗi khi xử lý API: " + e.getMessage());
        }
        return "redirect:/";
    }
    @GetMapping("/success")
    public String paySuccess(@RequestParam("paymentId") String paymentId,
                             @RequestParam("PayerID") String payerId,
                             Principal principal,
                             HttpSession session,
                             Model model) {
        try{
            Payment payment = payService.executePayment(paymentId, payerId);
            model.addAttribute("payment", payment);
            com.example.casestudy.model.Payment paymentSuccess = new com.example.casestudy.model.Payment();
            paymentSuccess.setPaymentDate(LocalDateTime.now());
            paymentSuccess.setPaymentMethod(PAYPAL);
            paymentSuccess.setStatus(COMPLETED);
            paymentSuccess.setOrder((Order) session.getAttribute("order"));
            paymentSuccess.setAmount((Double) session.getAttribute("totalAmount"));
            paymentService.savePayment(paymentSuccess);
            String username = principal.getName();
            Account account = accountService.findByUsername(username);
            if (account == null) {
                throw new RuntimeException("Không tìm thấy tài khoản của người dùng");
            }
            Customer customer = account.getCustomer();
            model.addAttribute("id", customer.getId());
            model.addAttribute("names", customer.getFullName());
            model.addAttribute("emails", customer.getEmail());
            Double total = (Double) session.getAttribute("totalAmount");
            if (total != null) {
                model.addAttribute("totalAmount", total);
            }
            emailService.sendPaymentSuccessEmail(customer.getEmail(), customer.getFullName(), paymentId, total);
            return "payment/success";
        } catch (PayPalRESTException  e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return "redirect:/checkout/add-to-checkout";
    }
    @GetMapping("/cancle")
    public String payCancle(){
        return "payment/cancle";
    }
    public Integer extractPaymentId(String paymentId) {
        try {
            String idStr = paymentId.replace("PAYID-", "").trim();
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid paymentId format: " + paymentId, e);
        }
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

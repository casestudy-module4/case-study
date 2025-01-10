package com.example.casestudy.controller;

import com.example.casestudy.dto.CategoryDTO;
import com.example.casestudy.dto.OrderDetailsDTO;
import com.example.casestudy.model.Customer;
import com.example.casestudy.model.Order;
import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.model.Product;
import com.example.casestudy.repository.*;
import com.example.casestudy.service.*;
import com.example.casestudy.service.implement.AccountService;
import com.example.casestudy.service.implement.EmailService;
import com.example.casestudy.service.implement.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class UserController {
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
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/products")
    public String getProducts(@RequestParam(defaultValue = "") String name,
                              @RequestParam(name = "categoryId", required = false) Integer categoryId,
                              @RequestParam(name = "searchQuery", defaultValue = "") String searchQuery,
                              @RequestParam(name = "orderby", defaultValue = "default") String orderby,
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

        return "products";
    }


    @GetMapping("/products/details")
    @ResponseBody
    public Product getProductDetails(@RequestParam Integer productId) {
        return productService.findById(productId);
    }

    @PostMapping("/cart/add")
    public @ResponseBody
    UserController.Response addToCart(@RequestBody OrderDetailsDTO cartItemDTO, HttpSession session) {
        try {
            // Kiểm tra nếu có session với đơn hàng (order)
            Order order = (Order) session.getAttribute("order");
            if (order == null) {
                order = new Order();  // Nếu chưa có order, tạo một order mới
                orderRepository.save(order);  // Lưu order vào DB
                session.setAttribute("order", order);  // Lưu vào session
            }

            // Lấy sản phẩm từ service
            Product product = productService.getProductById(cartItemDTO.getProductId());
            if (product == null) {
                return new UserController.Response(false, "Sản phẩm không tồn tại");
            }

            // Kiểm tra xem sản phẩm có trong giỏ hàng chưa, nếu chưa thì tạo mới OrderDetails
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setProduct(product);
            orderDetails.setQuantity(cartItemDTO.getQuantity());
            orderDetails.setPriceDetailOrder(product.getPrice());

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
        Integer customerId = 1;  // Giả sử bạn lấy customerId = 1

        // Lấy danh sách chi tiết đơn hàng (orderDetails) từ cơ sở dữ liệu
        List<OrderDetails> cartItems = orderDetailsRepository.findAllByOrderCustomerId(customerId);

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

        // Lưu giỏ hàng thành đơn hàng
        cartService.saveCartToOrder(customerId);

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

}

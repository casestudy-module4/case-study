//package com.example.casestudy.controller;
//
//import com.example.casestudy.service.IOrderService;
//import com.example.casestudy.service.IPayService;
//import com.example.casestudy.service.IProductService;
//import com.paypal.api.payments.Links;
//import com.paypal.api.payments.Payment;
//import com.paypal.base.rest.PayPalRESTException;
//import jakarta.validation.constraints.Email;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/checkout")
//public class CheckoutController {
//    private static final String EXCHANGE_RATE_API = "https://open.er-api.com/v6/latest/USD";
//    private static final String SUCCESS_URL = "http://localhost:8080/success";
//    private static final String CANCEL_URL = "http://localhost:8080/cancel";
//    @Autowired
//    private IProductService productService;
//    @Autowired
//    private IPayService payService;
//    @Autowired
//    private CartService cartService;
//    @Autowired
//    private IOrderService orderService;
//    @GetMapping("")
//    public String getInformationPay(Model model){
//        model.addAttribute("cartTotal", 0);
//        return "checkout";
//    }
//    @PostMapping("/add-to-checkout")
//    public String processCheckout(
//            @RequestParam("productIds") List<Integer> productIds,
//            @RequestParam("quantities") List<Integer> quantities,
//            @RequestParam("name") String name,
//            @RequestParam("phone") String phone,
//            @RequestParam("email") String email,
//            Model model) {
//        if (productIds == null || productIds.isEmpty() || quantities == null || quantities.size() != productIds.size()) {
//            model.addAttribute("message", "Dữ liệu không hợp lệ để thanh toán.");
//            return "checkout";
//        }
//        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
//            model.addAttribute("message", "Vui lòng điền đầy đủ thông tin khách hàng.");
//            return "checkout";
//        }
//        try {
//            orderService.processCheckout(productIds, quantities, name, phone, email);
//        } catch (IllegalArgumentException e) {
//            model.addAttribute("error", e.getMessage());
//            return "checkout";
//        }
//        return "redirect:/checkout/process-payment";
//    }
//    @PostMapping("/process-payment")
//    public String processPayment( @RequestParam("total") Double total){
//        try{
//            RestTemplate restTemplate = new RestTemplate();
//            String response = restTemplate.getForObject(EXCHANGE_RATE_API, String.class);
//            System.out.println("Response từ API: " + response);
//            JSONObject jsonResponse = new JSONObject(response);
//            if (jsonResponse.has("rates")) {
//                JSONObject rates = jsonResponse.getJSONObject("rates");
//                if (rates.has("VND")) {
//                    double exchangeRate = rates.getDouble("VND");
//                    double convertedAmount = total / exchangeRate;
//                    Payment payment = payService.createPaymentWithPaypal(
//                            convertedAmount,
//                            "USD",
//                            "paypal",
//                            "sale",
//                            CANCEL_URL,
//                            SUCCESS_URL);
//
//                    for (Links link : payment.getLinks()) {
//                        if (link.getRel().equals("approval_url")) {
//                            return "redirect:" + link.getHref();
//                        }
//                    }
//                } else {
//                    throw new RuntimeException("Tỷ giá VND không tồn tại trong phản hồi API.");
//                }
//            } else {
//                throw new RuntimeException("Phản hồi từ API không chứa trường 'rates'.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Đã xảy ra lỗi khi xử lý API: " + e.getMessage());
//        }
//        return "redirect:/";
//    }
//    @GetMapping("/success")
//    public String paySuccess(@RequestParam("paymentId") String paymentId,
//                                  @RequestParam("payerId") String payerId, Model model) {
//        try{
//            Payment payment = payService.executePayment(paymentId, payerId);
//            model.addAttribute("payment", payment);
//            return "payment/success";
//        } catch (PayPalRESTException e) {
//            e.printStackTrace();
//        }
//        return "redirect:/checkout/add-to-checkout";
//    }
//    @GetMapping("/cancle")
//    public String payCancle(){
//        return "payment/cancle";
//    }
//}

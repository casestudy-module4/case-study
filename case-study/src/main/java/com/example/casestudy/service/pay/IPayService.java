package com.example.casestudy.service.pay;

public interface IPayService {
    String createPaymentWithPaypal(Double total, String cancelUrl, String successUrl);

}

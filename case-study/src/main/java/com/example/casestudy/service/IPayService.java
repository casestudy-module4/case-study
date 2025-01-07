package com.example.casestudy.service;

public interface IPayService {
    String createPaymentWithPaypal(Double total, String cancelUrl, String successUrl);
}
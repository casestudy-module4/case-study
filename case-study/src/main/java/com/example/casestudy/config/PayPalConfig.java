//package com.example.casestudy.config;
//
//import com.paypal.base.rest.APIContext;
//import com.paypal.base.rest.PayPalRESTException;
//import lombok.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class PayPalConfig {
//    @Value("${paypal.client.id}")
//    private String clientId;
//
//    @Value("${paypal.client.secret}")
//    private String clientSecret;
//
//    @Value("${paypal.mode}")
//    private String mode;
//
//    @Bean
//    public APIContext apiContext() throws PayPalRESTException {
//        return new APIContext(clientId, clientSecret, mode);
//    }
//}

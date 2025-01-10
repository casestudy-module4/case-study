package com.example.casestudy.config;

import com.example.casestudy.model.Customer;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Customer getLoggedCustomer(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof Customer){
            return (Customer) authentication.getPrincipal();
        }
        return null;
    }
}

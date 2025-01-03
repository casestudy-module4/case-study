package com.example.casestudy.service;

import com.example.casestudy.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerService {
    List<Customer> findAll();
    void deleteCustomer(int id);
    Page<Customer> findByTitle(String fullName, Integer page);
    Customer findById(int id);
}

package com.example.casestudy.service;

import com.example.casestudy.model.Customer;

public interface ICustomerServicee {
    Customer findById(int id);
    void save(Customer customer);
}

package com.example.casestudy.service;

import com.example.casestudy.model.Customer;

public interface ICustomerServicee {
    Customer findById(Integer id);

    void update(Customer customer);

    Object findByName(String fullName, int page);

    ;
}

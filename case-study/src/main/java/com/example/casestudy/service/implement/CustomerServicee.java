package com.example.casestudy.service.implement;

import com.example.casestudy.model.Customer;
import com.example.casestudy.repository.ICustomerRepository;
import com.example.casestudy.service.ICustomerServicee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServicee implements ICustomerServicee {

    @Autowired
    private ICustomerRepository customerRepository;

    @Override
    public Customer findById(int id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }
}

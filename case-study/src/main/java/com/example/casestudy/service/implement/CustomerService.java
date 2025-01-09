package com.example.casestudy.service.implement;

import com.example.casestudy.model.Customer;
import com.example.casestudy.repository.CustomerRepository;
import com.example.casestudy.service.ICustomerService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Page<Customer> findByTitle(String fullName, Integer page) {
        return customerRepository.findAllByFullNameContainingIgnoreCase(fullName, PageRequest.of(page, 5));
    }
    public Customer findById(int id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer findByUsername(String username) {
        return null;
    }


}

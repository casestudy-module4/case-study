package com.example.casestudy.service.implement;

import com.example.casestudy.model.Account;
import com.example.casestudy.model.Customer;
import com.example.casestudy.repository.AccountRepository;
import com.example.casestudy.repository.CustomerRepository;
import com.example.casestudy.service.ICustomerService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService implements ICustomerService {
    @Autowired
    private AccountRepository accountRepository;


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
        Account account = accountRepository.findByResName(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return account.getCustomer();
    }

    @Override
    public void updateCustomer(Customer customer) {
        Customer existingCustomer = customerRepository.findById(customer.getId()).orElse(null);
        if (existingCustomer != null) {
            existingCustomer.setFullName(customer.getFullName());
            existingCustomer.setPhoneNumber(customer.getPhoneNumber());
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setGender(customer.getGender());
            existingCustomer.setBirthdate(customer.getBirthdate());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setImage(customer.getImage());
            customerRepository.save(existingCustomer);
        }
    }

    @Override
    public Customer getCurrentUser() {
        // Lấy thông tin xác thực từ SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("Không tìm thấy người dùng đã đăng nhập.");
        }

        // Lấy username của người dùng hiện tại từ Authentication
        String username = authentication.getName();

        // Tìm Customer từ cơ sở dữ liệu dựa vào username
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với username: " + username));
    }
}
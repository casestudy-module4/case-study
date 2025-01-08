
package com.example.casestudy.service.implement;

import com.example.casestudy.dto.CustomerDTO;
import com.example.casestudy.model.Customer;
import com.example.casestudy.repository.CustomerRepository;
import com.example.casestudy.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public CustomerDTO getCustomerById() {
        Customer customer = customerRepository.findById(1).orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        return new CustomerDTO(
                customer.getId(),
                customer.getFullName(),
                customer.getPhoneNumber(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getGender(),
                customer.getBirthdate(),
                customer.getImage()
        );
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(customerDTO.getId()).orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        customer.setFullName(customerDTO.getFullName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setEmail(customerDTO.getEmail());
        customer.setAddress(customerDTO.getAddress());
        customer.setGender(customerDTO.getGender());
        customer.setBirthdate(customerDTO.getBirthdate());
        customer.setImage(customerDTO.getImage());
        customerRepository.save(customer);
    }
}
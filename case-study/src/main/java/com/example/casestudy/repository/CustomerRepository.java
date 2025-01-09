package com.example.casestudy.repository;

import com.example.casestudy.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CustomerRepository  extends JpaRepository<Customer,Integer> {
    Page<Customer> findAllByFullNameContainingIgnoreCase(String fullName, Pageable page);


}

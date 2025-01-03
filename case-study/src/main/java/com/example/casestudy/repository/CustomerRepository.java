package com.example.casestudy.repository;

import com.example.casestudy.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository  extends JpaRepository<Customer,Integer> {
    Page<Customer> findAllByFullNameContainingIgnoreCase(String fullName, Pageable page);
    @Query("SELECT c.address, COUNT(c.id) FROM customers c GROUP BY c.address")
    List<Object[]> countCustomersByAddress();

    @Query("SELECT c.gender, AVG(c.age) FROM customers c WHERE c.age IS NOT NULL GROUP BY c.gender")
    List<Object[]> calculateAverageAgeByGender();
}

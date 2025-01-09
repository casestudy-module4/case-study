package com.example.casestudy.repository;

import com.example.casestudy.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Page<Customer> findAllByFullNameContainingIgnoreCase(String fullName, Pageable page);

    @Query("SELECT a.customer FROM accounts a WHERE a.resName = :resName")
    Optional<Customer> findByUsername(@Param("resName") String resName);
}
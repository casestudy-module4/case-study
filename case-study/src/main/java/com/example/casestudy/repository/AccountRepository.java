package com.example.casestudy.repository;

import com.example.casestudy.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByResName(String resName);

    @Query("SELECT a FROM accounts a WHERE a.customer.email = :email")
    Account findByCustomerEmail(@Param("email") String email);
}

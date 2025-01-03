package com.example.casestudy.repository;

import com.example.casestudy.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByResName(String resName);
}

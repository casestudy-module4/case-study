package com.example.casestudy.service;

import com.example.casestudy.model.Account;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface IAccountService {
   List<Account> getAdmin();
    boolean logOut();
}

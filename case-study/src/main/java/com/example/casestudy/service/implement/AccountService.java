package com.example.casestudy.service.implement;

import com.example.casestudy.dto.UserInfoUserDetails;
import com.example.casestudy.model.Account;
import com.example.casestudy.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountRepository iAccountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveAccount(Account account) {
        account.setResPassword(passwordEncoder.encode(account.getResPassword()));
        iAccountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account appUser = iAccountRepository.findByResName(username);
//        Lấy tất cả role của AppUser
        UserInfoUserDetails infoUserDetails = new UserInfoUserDetails(appUser);
        return infoUserDetails;
    }
}

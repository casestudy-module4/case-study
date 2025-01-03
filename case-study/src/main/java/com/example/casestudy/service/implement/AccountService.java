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

    // Tìm account theo username
    public Account findByUsername(String username) {
        return iAccountRepository.findByResName(username);
    }

    // Đặt lại mật khẩu
    public boolean resetPassword(String username, String newPassword) {
        Account account = findByUsername(username);
        if (account == null) {
            return false; // Không tìm thấy tài khoản
        }
        if (!newPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\\$%\\^&\\*])(?=\\S+$).{8,20}$")) {
            throw new IllegalArgumentException("Password must be between 8-20 characters, include uppercase, lowercase, number, and special character.");
        }
        // Cập nhật mật khẩu mới (mã hóa)
        account.setResPassword(passwordEncoder.encode(newPassword));
        iAccountRepository.save(account);
        return true;
    }
}

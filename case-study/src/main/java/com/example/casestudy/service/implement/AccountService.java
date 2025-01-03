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

import java.time.LocalDateTime;

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
        UserInfoUserDetails infoUserDetails = new UserInfoUserDetails(appUser);
        return infoUserDetails;
    }

    public Account findByUsername(String username) {
        return iAccountRepository.findByResName(username);
    }

    public Account findByEmail(String email) {
        return iAccountRepository.findByCustomerEmail(email);
    }

    public void generateOtp(Account account) {
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000); // Tạo OTP 6 chữ số
        account.setOtp(otp);
        account.setOtpExpiry(LocalDateTime.now().plusMinutes(5)); // Thời gian hết hạn 5 phút
        iAccountRepository.save(account);
    }

    public boolean validateOtp(Account account, String otp) {
        if (account.getOtp() == null || !account.getOtp().equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP.");
        }
        if (account.getOtpExpiry() == null || account.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP has expired.");
        }
        return true;
    }

    public void clearOtp(Account account) {
        account.setOtp(null);
        account.setOtpExpiry(null);
        iAccountRepository.save(account);
    }

    public boolean resetPassword(String email, String newPassword) {
        Account account = findByEmail(email);
        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }
        if (!newPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\\$%\\^&\\*])(?=\\S+$).{8,20}$")) {
            throw new IllegalArgumentException("Password must meet security criteria.");
        }
        account.setResPassword(passwordEncoder.encode(newPassword));
        iAccountRepository.save(account);
        return true;
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        Account account = iAccountRepository.findByResName(username);

        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }
        if (!passwordEncoder.matches(currentPassword, account.getResPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        account.setResPassword(passwordEncoder.encode(newPassword));
        iAccountRepository.save(account);
    }
}

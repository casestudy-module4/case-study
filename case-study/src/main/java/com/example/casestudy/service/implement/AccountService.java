package com.example.casestudy.service.implement;

import com.example.casestudy.dto.UserInfoUserDetails;
import com.example.casestudy.model.Account;
import com.example.casestudy.model.Customer;
import com.example.casestudy.repository.AccountRepository;
import com.example.casestudy.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService implements UserDetailsService, IAccountService {
    @Autowired
    private AccountRepository iAccountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveAccount(Account account) {
        account.setResPassword(passwordEncoder.encode(account.getResPassword()));
        iAccountRepository.save(account);
    }

//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Account appUser = iAccountRepository.findByResName(username);
//        UserInfoUserDetails infoUserDetails = new UserInfoUserDetails(appUser);
//        return infoUserDetails;
//    }
public void registerUser(Account account, String role) {
    // Kiểm tra tên đăng nhập đã tồn tại
    if (iAccountRepository.findByResName(account.getResName()) != null) {
        throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");
    }

    // Tạo Customer nếu chưa có
    if (account.getCustomer() == null) {
        Customer customer = new Customer();
        customer.setEmail(account.getEmail());
        account.setCustomer(customer);
    }

    // Mã hóa mật khẩu và gán vai trò
    account.setResPassword(passwordEncoder.encode(account.getResPassword()));
    account.setRole(role);

    // Lưu tài khoản và Customer (CascadeType.ALL đảm bảo cả hai được lưu)
    iAccountRepository.save(account);
}
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = iAccountRepository.findByResName(username);
        if (account == null) {
            throw new UsernameNotFoundException("Tên đăng nhập không tồn tại.");
        }
        return new User(account.getResName(), account.getResPassword(),
                List.of(new SimpleGrantedAuthority(account.getRole())));
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

    public Map<Integer, Integer> getAccountRegistrationsByMonth() {
        List<Object[]> results = iAccountRepository.findAccountRegistrationsByMonth();
        Map<Integer, Integer> registrationData = new HashMap<>();
        for (Object[] result : results) {
            registrationData.put((Integer) result[0], ((Number) result[1]).intValue());
        }
        return registrationData;
    }

    @Override
    public List<Account> findAll() {
        return iAccountRepository.findAll();
    }

}

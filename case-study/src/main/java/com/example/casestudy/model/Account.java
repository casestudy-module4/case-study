package com.example.casestudy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Please enter the username")
    private String resName;

    //    @NotEmpty(message = "Please enter the password")
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\\$%\\^&\\*])(?=\\S+$).{8,20}$",
//            message = "Password must be between 8-20 characters, contain at least one number, one uppercase letter, one lowercase letter, and one special character (!@#$%^&*).")
    private String resPassword;

    //    @Enumerated(EnumType.STRING)
    @Column(name = "role_user", nullable = false)
    private String role;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    private String otp;

    private LocalDateTime otpExpiry;

    // Getter cho email từ bảng Customer
    public String getEmail() {
        return customer != null ? customer.getEmail() : null;
    }
}

package com.example.casestudy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import javax.management.relation.Role;

@Entity(name="accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Please enter the user name")
    private String res_name;
    @NotEmpty(message = "Please enter the password")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\\$%\\^&\\*])(?=\\S+$).{8,20}$",
            message = "Password must be between 8-20 characters, contain at least one number, one uppercase letter, one lowercase letter, and one special character (!@#$%^&*).")
    private String res_password;
    @Enumerated(EnumType.STRING)
    @Column(name="role_user", nullable = false)
    private Role role;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;
    public enum Role {
        ADMIN,
        CUSTOMER
    }
}

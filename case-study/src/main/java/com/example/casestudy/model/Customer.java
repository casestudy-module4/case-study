package com.example.casestudy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Please enter the full name")
    @Column(columnDefinition = "VARCHAR(50)")
    private String fullName;
    @NotNull(message = "Please enter the phone number")
    private String phoneNumber;
    @Column(columnDefinition = "VARCHAR(100)")
    private String address;
    @Column(columnDefinition = "BOOLEAN")
    private boolean gender;
    private Integer age;
    @NotNull(message = "Please enter the email")
    @Pattern(regexp = "/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$/", message = "Invalid format, enter again")
    private String email;
    private String image;

}

package com.example.casestudy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

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

    @Min(value=0)
    @Max(value=1)
    @Column(columnDefinition = "TINYINT")
    private Integer gender;

    private LocalDate birthdate;
    @NotNull(message = "Please enter the email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;
    private String image;
    public int calculateAge() {
        LocalDate today = LocalDate.now();
        if (birthdate != null) {
            return Period.between(birthdate, today).getYears();
        }
        return 0;
    }
}

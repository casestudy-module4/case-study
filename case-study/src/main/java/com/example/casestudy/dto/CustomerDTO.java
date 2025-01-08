package com.example.casestudy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private int id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;
    private Integer gender;
    private LocalDate birthdate;
    private String image;
}

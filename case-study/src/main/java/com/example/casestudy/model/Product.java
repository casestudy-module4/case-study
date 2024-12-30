package com.example.casestudy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "Please enter the product name")
    @Column(columnDefinition = "VARCHAR(50)")
    private String name;
    @NotNull(message = "Enter the price")
    private Double price;
    @NotEmpty(message = "Please enter the description.")
    private String description;
    @NotNull(message = "Please enter product packaging date ")
    private LocalDate productPackagingDate;
    @NotNull(message = "Please enter the total product quantity")
    private Integer totalProductQuantity;
    @NotNull(message = "Please update image")
    private String image;
    @NotNull
    private Integer remainProductQuantity = totalProductQuantity;
    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id", nullable = false)
    private Category category;
}

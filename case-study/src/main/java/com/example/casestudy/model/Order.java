package com.example.casestudy.model;

import com.example.casestudy.model.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime timeOrder;

    @ManyToOne
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;

    @Column(length = 100)
    private String address;

    @Column(nullable = false)
    private Integer statusOrder;

    @Column(nullable = false)
    private Double totalPrice;
}

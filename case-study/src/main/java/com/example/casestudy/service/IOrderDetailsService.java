package com.example.casestudy.service;

import com.example.casestudy.model.OrderDetails;

import java.util.List;

public interface IOrderDetailsService {
    List<OrderDetails> findAll();
}

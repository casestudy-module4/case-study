package com.example.casestudy.service.implement;

import com.example.casestudy.model.OrderDetails;
import com.example.casestudy.repository.OrderDetailsRepository;
import com.example.casestudy.service.IOrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailsService  implements IOrderDetailsService {
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Override
    public List<OrderDetails> findAll() {
        return orderDetailsRepository.findAll();
    }
}

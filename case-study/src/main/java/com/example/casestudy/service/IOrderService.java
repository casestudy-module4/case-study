package com.example.casestudy.service;

import java.util.List;

public interface IOrderService {
    public long getDistinctCustomerCount();
    public List<Object[]> getCustomerOrderStatistics();
}

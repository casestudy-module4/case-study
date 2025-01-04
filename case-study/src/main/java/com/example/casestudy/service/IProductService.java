package com.example.casestudy.service;

import java.util.Map;

public interface IProductService {
    Map<String, Object> getMostPurchasedProduct();
    Map<Integer, Integer> getSalesByMonth();
}

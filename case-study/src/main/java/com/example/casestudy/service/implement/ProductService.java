package com.example.casestudy.service.implement;

import com.example.casestudy.repository.ProductRepository;
import com.example.casestudy.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    public Map<String, Object> getMostPurchasedProduct() {
        List<Object[]> results = productRepository.findMostPurchasedProducts();
        if (!results.isEmpty()) {
            Object[] topProduct = results.get(0);
            Map<String, Object> data = new HashMap<>();
            data.put("productName", topProduct[0]);
            data.put("quantitySold", topProduct[1]);
            return data;
        }
        Map<String, Object> defaultData = new HashMap<>();
        defaultData.put("productName", "N/A");
        defaultData.put("quantitySold", 0);
        return defaultData;
    }
    public Map<Integer, Integer> getSalesByMonth() {
        List<Object[]> results = productRepository.findSalesByMonth();
        Map<Integer, Integer> salesData = new HashMap<>();
        for (Object[] result : results) {
            salesData.put((Integer) result[0], ((Number) result[1]).intValue());
        }
        return salesData;
    }
}

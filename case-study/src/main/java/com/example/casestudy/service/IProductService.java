package com.example.casestudy.service;

import com.example.casestudy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IProductService {

    List<Product> getBestSellingProducts();

    Page<Product> getAllProducts(Pageable pageable);
}

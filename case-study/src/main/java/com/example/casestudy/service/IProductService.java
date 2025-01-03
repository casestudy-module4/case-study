package com.example.casestudy.service;

import com.example.casestudy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService extends IBaseService<Product> {
    Page<Product> findAll(String name, Integer pageable);
}

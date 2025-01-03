package com.example.casestudy.service;

import com.example.casestudy.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService extends IBaseService<Category> {
    Page<Category> findAllByName(String name, Pageable pageable);
}
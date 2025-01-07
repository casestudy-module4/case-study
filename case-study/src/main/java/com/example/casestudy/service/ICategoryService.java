package com.example.casestudy.service;

import com.example.casestudy.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService extends IBaseService<Category> {
    List<Category> findAll();
}

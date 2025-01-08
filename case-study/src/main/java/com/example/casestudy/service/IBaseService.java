package com.example.casestudy.service;

import com.example.casestudy.model.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IBaseService<T> {
    void addNew(T t );
    boolean update(int id, T t);
    List<T> getAll();
    T getById(int id);
    boolean deleteById(int id);
    T findById(int id);
    Page<T> findByName(String name, Integer page);
}

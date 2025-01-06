package com.example.casestudy.service;

import java.util.List;

public interface IBaseService<T> {
    void addNew(T t );
    boolean update(int id, T t);
    List<T> getAll();
    T getById(int id);
    boolean deleteById(int id);
    T findById(int id);
}

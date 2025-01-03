package com.example.casestudy.service.imple;

import com.example.casestudy.model.Category;
import com.example.casestudy.repository.CategoryRepository;
import com.example.casestudy.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Page<Category> findAllByName(String name, Pageable pageable) {
        return categoryRepository.findByNameCategory(name, pageable);
    }

    @Override
    public void addNew(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public boolean update(int id, Category category) {
        categoryRepository.save(category);
        return true;
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(int id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public boolean deleteById(int id) {
        categoryRepository.deleteById(id);
        return true;
    }

    @Override
    public Category findById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }
}

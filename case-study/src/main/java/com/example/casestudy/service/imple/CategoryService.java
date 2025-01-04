package com.example.casestudy.service.imple;

import com.example.casestudy.model.Category;
import com.example.casestudy.model.Product;
import com.example.casestudy.repository.CategoryRepository;
import com.example.casestudy.repository.ProductRepository;
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
    @Autowired
    private ProductRepository productRepository;
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
    @Transactional
    @Override
    public boolean deleteById(int categoryId) {
        List<Product> products = productRepository.findByCategory_Id(categoryId);
        Category defaultCategory = categoryRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Default category not found"));

        for (Product product : products) {
            product.setCategory(defaultCategory);
            productRepository.save(product);
        }
        categoryRepository.deleteById(categoryId);
    return true;
    }

    @Override
    public Category findById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }
}

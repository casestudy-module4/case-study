package com.example.casestudy.service.imple;

import com.example.casestudy.model.Product;
import com.example.casestudy.repository.ProductRepository;
import com.example.casestudy.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public Page<Product> findAll(String name, Integer pageable) {
        return productRepository.findAllByCategory_nameCategoryContainingIgnoreCase(name, PageRequest.of(pageable, 10));
    }
    @Override
    public void addNew(Product product) {
        productRepository.save(product);
    }

    @Override
    public boolean update(int id, Product product) {
        productRepository.save(product);
        return true;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getById(int id) {
        return productRepository.findById(id).get();
    }

    @Override
    public boolean deleteById(int id) {
        productRepository.deleteById(id);
        return true;
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id).orElse(null);
    }

}

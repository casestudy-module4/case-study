package com.example.casestudy.service;

import com.example.casestudy.model.Product;
import com.example.casestudy.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public Page<Product> searchProductsByName(String searchQuery, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(searchQuery, pageable);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }

    public List<Product> getTop4Products() {
        return productRepository.findTop4ByOrderByIdAsc();
    }

}

package com.example.casestudy.service;

import com.example.casestudy.model.Product;
import com.example.casestudy.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.sql.In;
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

    public Page<Product> getProductsByCategory(Integer categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public Page<Product> searchProductsByName(String searchQuery, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(searchQuery, pageable);
    }

    public Product getProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }

    public void updateProduct(Product product) {
        // Kiểm tra xem sản phẩm có tồn tại trong cơ sở dữ liệu không
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm để cập nhật"));

        // Cập nhật các thuộc tính
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setProductPackagingDate(product.getProductPackagingDate());
        existingProduct.setTotalProductQuantity(product.getTotalProductQuantity());
        existingProduct.setRemainProductQuantity(product.getRemainProductQuantity());
        existingProduct.setImage(product.getImage());
        existingProduct.setCategory(product.getCategory());

        // Lưu sản phẩm đã cập nhật
        productRepository.save(existingProduct);
    }
}

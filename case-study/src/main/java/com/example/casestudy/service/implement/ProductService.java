package com.example.casestudy.service.implement;

import com.example.casestudy.dto.ProductSalesDTO;
import com.example.casestudy.dto.TopProductDTO;
import com.example.casestudy.model.Product;
import com.example.casestudy.repository.ProductRepository;
import com.example.casestudy.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public Page<Product> findAll(String name, Integer pageable) {
        return productRepository.findAllByCategory_nameCategoryContainingIgnoreCase(name, PageRequest.of(pageable, 8));
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Integer remainProductCount(int idProduct) {
        return productRepository.findRemainProductQuantity(idProduct);
    }

    @Override
    public Page<Product> findByName(String name, Integer page) {
        return productRepository.findByNameContainingIgnoreCase(name, PageRequest.of(page, 5));
    }

    @Override
    public Page<Product> getProductsByCategory(Integer id, Pageable pageable) {
        return productRepository.findByCategoryId(id, pageable);
    }

    @Override
    public Page<Product> searchProductsByName(String searchName, Pageable pageable) {
        return null;
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
    public Map<String, Object> getMostPurchasedProduct() {
        List<Object[]> results = productRepository.findMostPurchasedProducts();
        if (!results.isEmpty()) {
            Object[] topProduct = results.get(0);
            Map<String, Object> data = new HashMap<>();
            data.put("productName", topProduct[0]);
            data.put("quantitySold", topProduct[1]);
            return data;
        }
        Map<String, Object> defaultData = new HashMap<>();
        defaultData.put("productName", "N/A");
        defaultData.put("quantitySold", 0);
        return defaultData;
    }
    public Map<Integer, Integer> getSalesByMonth() {
        List<Object[]> results = productRepository.findSalesByMonth();
        Map<Integer, Integer> salesData = new HashMap<>();
        for (Object[] result : results) {
            salesData.put((Integer) result[0], ((Number) result[1]).intValue());
        }
        return salesData;
    }

    @Override
    public List<TopProductDTO> getTopSellingOrDefaultProducts() {
        List<TopProductDTO> topSellingProducts = productRepository
                .findTopSellingProducts(PageRequest.of(0, 4))
                .getContent();
        if (topSellingProducts.isEmpty()) {
            List<Product> defaultProducts = productRepository
                    .findAll(PageRequest.of(0, 4))
                    .getContent();

            return defaultProducts.stream().map(product -> new TopProductDTO(
                    product,
                    product.getCategory(),
                    0L,
                    product.getImage()
            )).collect(Collectors.toList());
        }

        return topSellingProducts;
    }

    public Product getProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }

}
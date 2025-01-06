package com.example.casestudy.service.implement;

import com.example.casestudy.model.Product;
import com.example.casestudy.repository.IProductRepository;
import com.example.casestudy.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private IProductRepository productRepository;


//
//    @Override
//    public Page<Product> findAll(String name, Integer pageable) {
//        return productRepository.findAllByCategory_nameCategoryContainingIgnoreCase(name, PageRequest.of(pageable, 10));
//    }
//
//    @Override
//    public Integer remainProductCount(int idProduct) {
//        return productRepository.findRemainProductQuantity(idProduct);
//    }
//


    @Override
    public List<Product> getBestSellingProducts() {
        return productRepository.findTop4ByOrderByQuantitySoldDesc();
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

//
//    @Override
//    public void addNew(Product product) {
//        productRepository.save(product);
//    }
//
//    @Override
//    public boolean update(int id, Product product) {
//        productRepository.save(product);
//        return true;
//    }
//
//    @Override
//    public List<Product> getAll() {
//        return productRepository.findAll();
//    }
//
//    @Override
//    public Product getById(int id) {
//        return productRepository.findById(id).get();
//    }
//
//    @Override
//    public boolean deleteById(int id) {
//        productRepository.deleteById(id);
//        return true;
//    }
//
//    @Override
//    public Product findById(int id) {
//        return productRepository.findById(id).orElse(null);
//    }
//
//    public Map<String, Object> getMostPurchasedProduct() {
//        List<Object[]> results = productRepository.findMostPurchasedProducts();
//        if (!results.isEmpty()) {
//            Object[] topProduct = results.get(0);
//            Map<String, Object> data = new HashMap<>();
//            data.put("productName", topProduct[0]);
//            data.put("quantitySold", topProduct[1]);
//            return data;
//        }
//        Map<String, Object> defaultData = new HashMap<>();
//        defaultData.put("productName", "N/A");
//        defaultData.put("quantitySold", 0);
//        return defaultData;
//    }
//
//    public Map<Integer, Integer> getSalesByMonth() {
//        List<Object[]> results = productRepository.findSalesByMonth();
//        Map<Integer, Integer> salesData = new HashMap<>();
//        for (Object[] result : results) {
//            salesData.put((Integer) result[0], ((Number) result[1]).intValue());
//        }
//        return salesData;
//    }
}

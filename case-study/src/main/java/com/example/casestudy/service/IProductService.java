package com.example.casestudy.service;

import com.example.casestudy.dto.TopProductDTO;
import com.example.casestudy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IProductService extends IBaseService<Product> {
    Map<String, Object> getMostPurchasedProduct();

    Map<Integer, Integer> getSalesByMonth();

    Page<Product> findAll(String name, Integer pageable);

    Integer remainProductCount(int idProduct);
    Page<Product> findByName(String name, Integer page);

    Page<Product> getProductsByCategory(Integer id, Pageable pageable);

    Page<Product> searchProductsByName(String searchName,Pageable pageable);

    Page<Product> getAllProducts(Pageable pageable);

    List<TopProductDTO> getTopSellingOrDefaultProducts();

    Product getProductById(Integer productId);
}

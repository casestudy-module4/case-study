package com.example.casestudy.service;

import com.example.casestudy.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import com.example.casestudy.dto.TopProductDTO;
import java.util.List;

public interface IProductService extends IBaseService<Product>{
    Map<String, Object> getMostPurchasedProduct();
    Map<Integer, Integer> getSalesByMonth();
    Page<Product> findAll(String name, Integer pageable);
    Integer remainProductCount(int idProduct);
    List<TopProductDTO> getTopSellingOrDefaultProducts();
    Page<Product> getProductsByCategory(Integer id, Pageable pageable);
    Page<Product> searchProductsByName(String searchName,Pageable pageable);

}

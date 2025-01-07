package com.example.casestudy.service.implement;

import com.example.casestudy.dto.TopProductDTO;
import com.example.casestudy.model.Product;
import com.example.casestudy.repository.IProductRepository;
import com.example.casestudy.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository iproductRepository;

    @Override
    public List<TopProductDTO> getTopSellingOrDefaultProducts() {
        List<TopProductDTO> topSellingProducts = iproductRepository
                .findTopSellingProducts(PageRequest.of(0, 4))
                .getContent();
        if (topSellingProducts.isEmpty()) {
            List<Product> defaultProducts = iproductRepository
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
}

package com.example.casestudy.dto;

import com.example.casestudy.model.Product;
import com.example.casestudy.model.Category;

public class TopProductDTO {
    private Product product;
    private Category category;
    private Long totalQuantityOrdered;
    private String img;


    public TopProductDTO(Product product, Category category, Long totalQuantityOrdered, String img) {
        this.product = product;
        this.category = category;
        this.totalQuantityOrdered = totalQuantityOrdered;
        this.img = img;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getTotalQuantityOrdered() {
        return totalQuantityOrdered;
    }

    public void setTotalQuantityOrdered(Long totalQuantityOrdered) {
        this.totalQuantityOrdered = totalQuantityOrdered;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}

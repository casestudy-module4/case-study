package com.example.casestudy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductSalesDTO {
    private Integer productId;
    private String productName;
    private Long totalSold;
}

package com.example.casestudy.service;

import com.example.casestudy.dto.TopProductDTO;

import java.util.List;

public interface IProductServicee  {
    List<TopProductDTO> getTopSellingOrDefaultProducts();
}

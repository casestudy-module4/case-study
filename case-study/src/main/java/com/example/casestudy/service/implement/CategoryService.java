package com.example.casestudy.service.implement;

import com.example.casestudy.dto.CategoryDTO;
import com.example.casestudy.model.Category;
import com.example.casestudy.model.Product;
import com.example.casestudy.repository.CategoryRepository;
import com.example.casestudy.repository.ProductRepository;
import com.example.casestudy.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void addNew(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public boolean update(int id, Category category) {
        categoryRepository.save(category);
        return true;
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(int id) {
        return categoryRepository.findById(id).get();
    }
    @Transactional
    @Override
    public boolean deleteById(int categoryId) {
        List<Product> products = productRepository.findByCategory_Id(categoryId);
        Category defaultCategory = categoryRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Default category not found"));

        for (Product product : products) {
            product.setCategory(defaultCategory);
            productRepository.save(product);
        }
        categoryRepository.deleteById(categoryId);
    return true;
    }

    @Override
    public Category findById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Category> findByName(String name, Integer page) {
        return categoryRepository.findAllByNameCategoryContainingIgnoreCase(name, PageRequest.of(page, 5));
    }



    @Override
    public List<CategoryDTO> getAllCategoryDTOs() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDTO(
                        category.getId(),
                        category.getNameCategory(),
                        generateImageUrl(category.getNameCategory())
                ))
                .collect(Collectors.toList());
    }
    private String generateImageUrl(String nameCategory) {
        switch (nameCategory.toLowerCase()) {
            case "hiện đại":
                return "/img/hien-dai/mau-hop-qua-tet-HQT2025-1.jpg";
            case "truyền thống":
                return "/img/truyen-thong/mau-hop-dung-qua-tet-2023-1.jpg";
            case "linh vật":
                return "/img/linh-vat/mau-hop-dung-qua-tet-co-san-song-hac-do.jpg";
            case "chibi":
                return "/img/chibi/mau-hop-dung-qua-tet-2022-3.jpg";
            default:
                return "null";
        }
    }

}

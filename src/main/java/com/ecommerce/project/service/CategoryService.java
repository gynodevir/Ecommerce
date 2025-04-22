package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategory(Integer pageNumber,Integer pageSize);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryid);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}

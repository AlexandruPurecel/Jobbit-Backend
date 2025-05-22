package com.backend.jobbit.service.categoryService;

import com.backend.jobbit.dto.categoryDto.CategoryDto;
import com.backend.jobbit.persistence.model.Category;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto getCategoryById(Long id);
    List<CategoryDto> getAllCategories();
    CategoryDto updateCategory(CategoryDto categoryDto, Long id);
    void deleteCategory(Long id);
    CategoryDto convertToDto(Category category);
}

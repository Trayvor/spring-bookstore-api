package org.example.bookstore.service;

import java.util.List;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CreateCategoryDto;
import org.example.bookstore.dto.category.UpdateCategoryDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryDto createCategoryDto);

    CategoryDto update(Long id, UpdateCategoryDto updateCategoryDto);

    void deleteById(Long id);
}

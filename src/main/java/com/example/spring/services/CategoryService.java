package com.example.spring.services;

import com.example.spring.dto.BookDto;
import com.example.spring.dto.CategoryDto;
import com.example.spring.dto.CategoryResponseDto;
import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> findAll();

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryDto categoryDto);

    CategoryResponseDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);

    List<BookDto> getBooksByCategoriesId(Long id);
}

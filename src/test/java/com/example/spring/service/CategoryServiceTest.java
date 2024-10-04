package com.example.spring.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.spring.dto.CategoryDto;
import com.example.spring.dto.CategoryResponseDto;
import com.example.spring.mapper.CategoryMapper;
import com.example.spring.models.Category;
import com.example.spring.repositories.category.CategoryRepository;
import com.example.spring.services.CategoryServiceImpl;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Get all categories from store")
    public void getAll_GivenCategoriesInStore_ShouldReturnAllCategories() {
        // Given
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");
        category1.setDescription("Description 1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");
        category2.setDescription("Description 2");

        List<Category> categories = Arrays.asList(category1, category2);
        final List<CategoryDto> categoryDtos = Arrays.asList(
                new CategoryDto("Category 1", "Description 1"),
                new CategoryDto("Category 2", "Description 2")
        );
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toResponse(category1)).thenReturn(
                new CategoryResponseDto(category1.getId(), category1.getName(),
                        category1.getDescription()));
        when(categoryMapper.toResponse(category2)).thenReturn(
                new CategoryResponseDto(category2.getId(), category2.getName(),
                        category2.getDescription())
        );

        // When
        List<CategoryResponseDto> result = categoryService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(categoryDtos.get(0).name(), result.get(0).name());
        assertEquals(categoryDtos.get(0).description(), result.get(0).description());
        assertEquals(categoryDtos.get(1).name(), result.get(1).name());
        assertEquals(categoryDtos.get(1).description(), result.get(1).description());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw exception when invalid category data is provided")
    public void getAll_GivenInvalidCategoriesInStore_ShouldThrowException() {
        // Given
        Category invalidCategory = new Category();
        invalidCategory.setId(1L);
        invalidCategory.setName(null);
        invalidCategory.setDescription("Description 1");
        List<Category> invalidCategories = Arrays.asList(invalidCategory);
        when(categoryRepository.findAll()).thenReturn(invalidCategories);
        when(categoryMapper.toResponse(invalidCategory)).thenThrow(
                new IllegalArgumentException("Invalid category data"));

        // When & Then
        Exception exception = assertThrows(
                IllegalArgumentException.class, () -> {
                    categoryService.findAll();
                });
        assertEquals("Invalid category data", exception.getMessage());
        verify(categoryRepository, times(1)).findAll();
    }
}

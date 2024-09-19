package com.example.spring.mapper;

import com.example.spring.config.MapperConfig;
import com.example.spring.dto.CategoryDto;
import com.example.spring.dto.CategoryResponseDto;
import com.example.spring.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    void updateCategoryFromDto(CategoryDto categoryDto, @MappingTarget Category category);

    CategoryResponseDto toResponse(Category category);
}

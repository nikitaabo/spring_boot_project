package com.example.spring.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.spring.dto.CategoryDto;
import com.example.spring.dto.CategoryResponseDto;
import com.example.spring.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new category")
    void createCategory_ValidRequestDto_Success() throws Exception {
        // Given
        CategoryDto requestDto = new CategoryDto("Category 1", "Description");

        CategoryResponseDto expected = new CategoryResponseDto(1L, requestDto.name(),
                requestDto.description());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryResponseDto.class);
        checkAssertions(expected, actual);
    }

    void checkAssertions(CategoryResponseDto expected, CategoryResponseDto actual) {
        assertNotNull(actual);
        assertNotNull(actual.id());
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.description(), actual.description());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get a category by id")
    @Sql(
            scripts = {"classpath:database/categories/clear-categories.sql",
                    "classpath:database/categories/add-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/categories/delete-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getCategoryById_ValidId_Success() throws Exception {
        // Given
        CategoryResponseDto expected = new CategoryResponseDto(1L, "New Category", "Description");

        // When
        MvcResult result = mockMvc.perform(get("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryResponseDto.class);
        checkAssertions(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete a category by id")
    @Sql(
            scripts = {"classpath:database/categories/clear-categories.sql",
                    "classpath:database/categories/add-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/categories/delete-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void deleteCategoryById_ValidId_Success() throws Exception {
        // Given

        // When
        mockMvc.perform(delete("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());

        // Then
        mockMvc.perform(get("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof EntityNotFoundException,
                        "Expected EntityNotFoundException"));
        ;
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get all categories")
    @Sql(
            scripts = "classpath:database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/categories/clear-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getAll_GivenCategoriesInStore_ShouldReturnAllCategories() throws Exception {
        // Given
        List<CategoryResponseDto> expected = new ArrayList<>();
        CategoryResponseDto category1 = new CategoryResponseDto(1L, "Category 1", "Description 1");
        expected.add(category1);
        CategoryResponseDto category2 = new CategoryResponseDto(2L, "Category 2", "Description 2");
        expected.add(category2);
        CategoryResponseDto category3 = new CategoryResponseDto(3L, "Category 3", "Description 3");
        expected.add(category3);

        // When
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryResponseDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                CategoryResponseDto[].class);
        assertEquals(expected.size(), actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get a category by negative id")
    @Sql(
            scripts = {"classpath:database/categories/clear-categories.sql",
                    "classpath:database/categories/add-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/categories/delete-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getCategoryById_NegativeId_GetException() throws Exception {
        // Given
        long invalidId = -1L;

        // When
        mockMvc.perform(get("/categories/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof ConstraintViolationException,
                        "Expected ConstraintViolationException"));
        // Then
    }
}

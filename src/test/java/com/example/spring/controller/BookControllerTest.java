package com.example.spring.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.spring.dto.BookDto;
import com.example.spring.dto.CreateBookRequestDto;
import com.example.spring.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
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
@Sql(
        scripts = {
                "classpath:database/books/add-books.sql",
                "classpath:database/categories/add-categories.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
        scripts = {"classpath:database/books/clear-books-categories.sql",
                "classpath:database/categories/clear-categories.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
public class BookControllerTest {
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
    @DisplayName("Create a new book")
    void createBook_ValidRequestDto_Success() throws Exception {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Book 1");
        requestDto.setAuthor("Author");
        requestDto.setIsbn("12123123");
        requestDto.setPrice(BigDecimal.valueOf(50));
        requestDto.setDescription("Description");
        requestDto.setCoverImage("CoverImage");
        requestDto.setCategoriesIds(Set.of(1L, 2L));

        BookDto expected = new BookDto();
        expected.setTitle(requestDto.getTitle());
        expected.setAuthor(requestDto.getAuthor());
        expected.setIsbn(requestDto.getIsbn());
        expected.setPrice(requestDto.getPrice());
        expected.setDescription(requestDto.getDescription());
        expected.setCoverImage(requestDto.getCoverImage());
        expected.setCategoriesIds(requestDto.getCategoriesIds());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        checkAssertions(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get a book by id")
    void getBookById_ValidId_Success() throws Exception {
        // Given
        BookDto expected = createBookDto(0);
        expected.setCategoriesIds(null);

        // When
        MvcResult result = mockMvc.perform(get("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        checkAssertions(expected, actual);
    }

    void checkAssertions(BookDto expected, BookDto actual) {
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getIsbn(), actual.getIsbn());
        Assertions.assertEquals(0, expected.getPrice()
                .compareTo(actual.getPrice()), "Prices should be equal");
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCoverImage(), actual.getCoverImage());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete a book by id")
    void deleteBookById_ValidId_Success() throws Exception {
        // Given

        // When
        mockMvc.perform(delete("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());

        // Then
        mockMvc.perform(get("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof EntityNotFoundException,
                        "Expected EntityNotFoundException"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get all books")
    void getAll_GivenBooksInStore_ShouldReturnAllBooks() throws Exception {
        // Given
        List<BookDto> expected = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            BookDto book = createBookDto(i);
            expected.add(book);
        }

        // When
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                BookDto[].class);
        assertEquals(expected.size(), actual.length);
        reflectionEquals(expected, actual, "categoriesIds");
    }

    BookDto createBookDto(int number) {
        BookDto book = new BookDto();
        book.setId((long) number);
        book.setTitle("Book " + (number + 1));
        book.setAuthor("Author " + (number + 1));
        book.setIsbn(String.valueOf(number + 1).repeat(10));
        book.setPrice(BigDecimal.valueOf(11 + number * 5L));
        book.setDescription("Description for Book " + (number + 1));
        book.setCoverImage("cover_image_" + (number + 1) + ".jpg");
        return book;
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get a book by negative id")
    void getBookById_NegativeId_GetException() throws Exception {
        // Given
        long invalidId = -1L;

        // When
        mockMvc.perform(get("/books/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof ConstraintViolationException,
                        "Expected ConstraintViolationException"));
        // Then
    }
}

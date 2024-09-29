package com.example.spring.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.spring.dto.BookDto;
import com.example.spring.dto.CreateBookRequestDto;
import com.example.spring.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

        mockMvc.perform(post("/categories")
                        .content("{\"name\":\"Category 1\"}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(post("/categories")
                        .content("{\"name\":\"Category 2\"}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
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
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assertions.assertEquals(expected.getIsbn(), actual.getIsbn());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getCoverImage(), actual.getCoverImage());
        Assertions.assertEquals(expected.getCategoriesIds(), actual.getCategoriesIds());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get a book by id")
    @Sql(
            scripts = {"classpath:database/books/clear-books.sql",
                    "classpath:database/books/add-book.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/delete-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getBookById_ValidId_Success() throws Exception {
        // Given
        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("Book Title");
        expected.setAuthor("Author Name");
        expected.setIsbn("1234567890");
        expected.setPrice(BigDecimal.valueOf(20));
        expected.setDescription("Book description");
        expected.setCoverImage("cover_image_url.jpg");
        expected.setCategoriesIds(null);

        // When
        MvcResult result = mockMvc.perform(get("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assertions.assertEquals(expected.getIsbn(), actual.getIsbn());
        Assertions.assertEquals(0, expected.getPrice()
                .compareTo(actual.getPrice()), "Prices should be equal");
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getCoverImage(), actual.getCoverImage());

    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete a book by id")
    @Sql(
            scripts = {"classpath:database/books/clear-books.sql",
                    "classpath:database/books/add-book.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/delete-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
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
                .andExpect(result -> Assertions.assertTrue(
                        result.getResolvedException() instanceof EntityNotFoundException,
                        "Expected EntityNotFoundException"));
        ;
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get all books")
    @Sql(
            scripts = "classpath:database/books/add-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/clear-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getAll_GivenBooksInStore_ShouldReturnAllBooks() throws Exception {
        // Given
        BookDto book1 = new BookDto();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("1111111111");
        book1.setPrice(BigDecimal.valueOf(1));
        book1.setDescription("Description for Book 1");
        book1.setCoverImage("cover_image_1.jpg");

        BookDto book2 = new BookDto();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("2222222222");
        book2.setPrice(BigDecimal.valueOf(16));
        book2.setDescription("Description for Book 2");
        book2.setCoverImage("cover_image_2.jpg");

        BookDto book3 = new BookDto();
        book3.setId(3L);
        book3.setTitle("Book 3");
        book3.setAuthor("Author 3");
        book3.setIsbn("3333333333");
        book3.setPrice(BigDecimal.valueOf(21));
        book3.setDescription("Description for Book 3");
        book3.setCoverImage("cover_image_3.jpg");

        List<BookDto> expected = new ArrayList<>();
        expected.add(book1);
        expected.add(book2);
        expected.add(book3);

        // When
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
                BookDto[].class);
        Assertions.assertEquals(expected.size(), actual.length);
        EqualsBuilder.reflectionEquals(expected, actual, "categoriesIds");
    }
}

package com.example.spring.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.spring.dto.BookDto;
import com.example.spring.mapper.BookMapper;
import com.example.spring.models.Book;
import com.example.spring.repositories.book.BookRepository;
import com.example.spring.services.BookServiceImpl;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Get all books from store")
    void getAll_GivenBooksInStore_ShouldReturnAllBooks() {
        // Given
        List<Book> books = BookServiceTestUtil.getBooks();
        List<BookDto> bookDtos = BookServiceTestUtil.getBookDtos();
        Page<Book> pageBooks = new PageImpl<>(books);
        when(bookRepository.findAll(Pageable.unpaged())).thenReturn(pageBooks);
        when(bookMapper.toDto(books.get(0))).thenReturn(bookDtos.get(0));
        when(bookMapper.toDto(books.get(1))).thenReturn(bookDtos.get(1));

        // When
        List<BookDto> result = bookService.findAll(Pageable.unpaged());

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(bookDtos.get(0).getId(), result.get(0).getId());
        assertEquals(bookDtos.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(bookDtos.get(1).getId(), result.get(1).getId());
        assertEquals(bookDtos.get(1).getTitle(), result.get(1).getTitle());
        verify(bookRepository, times(1)).findAll(Pageable.unpaged());
    }

    @Test
    @DisplayName("Should throw exception when invalid data is provided")
    void getAll_GivenInvalidBooksInStore_ShouldThrowException() {
        // Given
        Book invalidBook = new Book();
        invalidBook.setId(1L);
        invalidBook.setTitle("");
        invalidBook.setAuthor("Author 1");
        invalidBook.setIsbn("1111111111");
        invalidBook.setPrice(BigDecimal.valueOf(-10.99));
        invalidBook.setDescription("Description 1");
        invalidBook.setCoverImage("cover_image_1.jpg");
        List<Book> invalidBooks = Arrays.asList(invalidBook);
        Page<Book> pageInvalidBooks = new PageImpl<>(invalidBooks);
        when(bookRepository.findAll(Pageable.unpaged())).thenReturn(pageInvalidBooks);
        when(bookMapper.toDto(invalidBook)).thenThrow(
                new IllegalArgumentException("Invalid book data"));

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookService.findAll(Pageable.unpaged());
        });
        assertEquals("Invalid book data", exception.getMessage());
        verify(bookRepository, times(1)).findAll(Pageable.unpaged());
    }
}

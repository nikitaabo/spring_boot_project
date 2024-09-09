package com.example.spring.services;

import com.example.spring.dto.BookDto;
import com.example.spring.dto.BookSearchParametersDto;
import com.example.spring.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto update(Long id, CreateBookRequestDto newBookDto);

    List<BookDto> search(BookSearchParametersDto bookSearchParametersDto);
}

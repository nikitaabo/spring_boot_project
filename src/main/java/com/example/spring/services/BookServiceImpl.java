package com.example.spring.services;

import com.example.spring.dto.BookDto;
import com.example.spring.dto.BookSearchParametersDto;
import com.example.spring.dto.CreateBookRequestDto;
import com.example.spring.exception.EntityNotFoundException;
import com.example.spring.mapper.BookMapper;
import com.example.spring.models.Book;
import com.example.spring.repositories.SpecificationBuilder;
import com.example.spring.repositories.book.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final SpecificationBuilder<Book> specificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        Book book = bookMapper.toModel(bookDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find element by id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto newBookDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("There's not book with id: " + id));
        bookMapper.updateBookFromDto(newBookDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> specification = specificationBuilder.build(bookSearchParametersDto);
        return bookRepository.findAll(specification).stream()
                .map(bookMapper::toDto)
                .toList();
    }
}

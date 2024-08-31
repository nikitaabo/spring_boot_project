package com.example.spring.services;

import com.example.spring.models.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}

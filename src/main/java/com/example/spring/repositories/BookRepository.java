package com.example.spring.repositories;

import com.example.spring.models.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}

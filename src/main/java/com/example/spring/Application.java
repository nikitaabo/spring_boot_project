package com.example.spring;

import com.example.spring.models.Book;
import com.example.spring.services.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setTitle("Potter");
                book.setAuthor("Author");
                book.setDescription("Awesome book");
                book.setPrice(BigDecimal.valueOf(199));
                book.setCoverImage("Image");
                book.setIsbn("isbn");
                Book savedBook = bookService.save(book);
                System.out.println(savedBook);
            }
        };
    }
}

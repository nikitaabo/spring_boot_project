package com.example.spring.validator;

import com.example.spring.repositories.book.BookRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueIsbnValidator implements ConstraintValidator<UniqueIsbn, String> {

    private final BookRepository bookRepository;

    @Override
    public void initialize(UniqueIsbn constraintAnnotation) {
    }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        return isbn != null && !bookRepository.existsByIsbn(isbn);
    }
}


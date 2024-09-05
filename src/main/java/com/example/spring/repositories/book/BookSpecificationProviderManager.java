package com.example.spring.repositories.book;

import com.example.spring.models.Book;
import com.example.spring.repositories.SpecificationProvider;
import com.example.spring.repositories.SpecificationProviderManager;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> specificationProviderList;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return specificationProviderList.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst().orElseThrow(
                        () -> new NoSuchElementException("Couldn't find specification with key: "
                                + key));
    }
}

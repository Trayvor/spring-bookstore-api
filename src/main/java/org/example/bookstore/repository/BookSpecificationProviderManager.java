package org.example.bookstore.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.exception.KeyNotFoundException;
import org.example.bookstore.model.Book;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new KeyNotFoundException("Can't find correct specification "
                        + "provider with key " + key));
    }
}

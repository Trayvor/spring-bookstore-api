package org.example.bookstore.repository.book.spec;

import org.example.bookstore.model.Book;
import org.example.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    public static final String SQL_WILDCARD = "%";

    @Override
    public String getKey() {
        return "title";
    }

    @Override
    public Specification<Book> getSpecification(String param) {
        String pattern = SQL_WILDCARD + param + SQL_WILDCARD;
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                        pattern.toLowerCase());
    }
}

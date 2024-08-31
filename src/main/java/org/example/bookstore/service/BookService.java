package org.example.bookstore.service;

import java.util.List;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookSearchParameters;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.dto.book.UpdateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto getById(Long id);

    BookDto updateById(Long id, UpdateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters params);

    List<BookDto> findByCategory(Long id);
}

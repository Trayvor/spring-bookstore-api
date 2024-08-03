package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.BookDto;
import org.example.bookstore.dto.BookSearchParameters;
import org.example.bookstore.dto.CreateBookRequestDto;
import org.example.bookstore.dto.UpdateBookRequestDto;
import org.example.bookstore.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoint for mapping books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Find all books", description = "Find all products wit sorting or "
            + "pagination")
    public List<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find book by id", description = "Find book by its id, you need to put "
            + "id as path variable")
    public BookDto getBookByID(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create book", description = "Book creation, you need to put book in "
            + "JSON format in request body")
    public BookDto createBook(@RequestBody @Validated CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Updating book by id, you should put id as "
            + "path variable and book in request body")
    public BookDto updateBook(@PathVariable Long id, @RequestBody UpdateBookRequestDto requestDto) {
        return bookService.updateById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by id", description = "Deleting book by id, you should put "
            + "id as path variable")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by parameters", description = "Searching books by "
            + "parameters title or author")
    public List<BookDto> searchBooks(BookSearchParameters searchParameters) {
        return bookService.search(searchParameters);
    }

}

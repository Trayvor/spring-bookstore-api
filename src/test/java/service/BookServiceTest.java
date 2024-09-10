package service;

import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.book.BookSpecificationBuilder;
import org.example.bookstore.repository.cart.item.CartItemRepository;
import org.example.bookstore.service.BookService;
import org.example.bookstore.service.impl.BookServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("""
            Verify save() method works correctly and returns correct BookDto with valid input data
            """)
    public void save_WithValidCreateBookRequestDto_ShouldReturnValidBookDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category name");
        category.setDescription("Category description");

        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("Title");
        createBookRequestDto.setAuthor("Author");
        createBookRequestDto.setDescription("Description");
        createBookRequestDto.setPrice(BigDecimal.TEN);
        createBookRequestDto.setCoverImage("Cover image");
        createBookRequestDto.setIsbn("0-7584-3034-5");
        createBookRequestDto.setCategoriesIds(List.of(category.getId()));

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setDescription("Description");
        book.setPrice(BigDecimal.TEN);
        book.setCoverImage("Cover image");
        book.setIsbn("0-7584-3034-5");
        book.setCategories(Set.of(category));

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setDescription(book.getDescription());
        bookDto.setPrice(book.getPrice());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setCategoriesIds(createBookRequestDto.getCategoriesIds());

        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.save(createBookRequestDto);

        assertEquals(bookDto, result);
        verify(bookMapper, times(1)).toModel(createBookRequestDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
    }


}

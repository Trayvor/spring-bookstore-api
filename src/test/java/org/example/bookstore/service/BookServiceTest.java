package org.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookSearchParameters;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.dto.book.UpdateBookRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.book.BookSpecificationBuilder;
import org.example.bookstore.service.impl.BookServiceImpl;
import org.example.bookstore.util.BookUtil;
import org.example.bookstore.util.CategoryUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @DisplayName("""
            Verify save() method works correctly and returns correct BookDto with valid input data
            """)
    @Test
    public void save_WithValidCreateBookRequestDto_ShouldReturnValidBookDto() {
        CreateBookRequestDto createBookRequestDto = BookUtil.createCreateFirstBookBookRequestDto();
        Book book = BookUtil.createFirstBook();
        BookDto bookDto = BookUtil.createFirstBookDto();

        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.save(createBookRequestDto);

        assertEquals(bookDto, result);
        verify(bookMapper, times(1)).toModel(any());
        verify(bookRepository, times(1)).save(any());
        verify(bookMapper, times(1)).toDto(any());
    }

    @DisplayName("""
            Verify updateById() method works correctly and returns correct BookDto with valid
            input data
            """)
    @Test
    public void updateById_WithValidIdAndUpdateBookDto_ShouldReturnCorrectBookDto() {
        Book book = BookUtil.createFirstBook();
        Category category = CategoryUtil.createFirstCategory();
        UpdateBookRequestDto updateBookRequestDto = BookUtil.createUpdateToSecondBookRequestDto();
        BookDto bookDto = BookUtil.createFirstBookDto();

        doReturn(Optional.of(book)).when(bookRepository).findById(book.getId());
        doAnswer(invocationOnMock -> {
            UpdateBookRequestDto invocationUpdateBookDto =
                    (UpdateBookRequestDto) invocationOnMock.getArgument(0);
            Book invocationBook = (Book) invocationOnMock.getArgument(1);

            invocationBook.setTitle(invocationUpdateBookDto.getTitle());
            invocationBook.setAuthor(invocationUpdateBookDto.getAuthor());
            invocationBook.setDescription(invocationUpdateBookDto.getDescription());
            invocationBook.setPrice(invocationUpdateBookDto.getPrice());
            invocationBook.setIsbn(invocationUpdateBookDto.getIsbn());
            invocationBook.setCategories(Set.of(category));

            return null;
        }).when(bookMapper).updateBookFromRequestDto(updateBookRequestDto, book);
        doReturn(book).when(bookRepository).save(book);
        doReturn(bookDto).when(bookMapper).toDto(book);

        BookDto result = bookService.updateById(book.getId(), updateBookRequestDto);

        assertEquals(bookDto, result);
        verify(bookMapper, times(1)).toDto(any());
        verify(bookRepository, times(1)).save(any());
        verify(bookMapper, times(1))
                .updateBookFromRequestDto(any(), any());
        verify(bookRepository, times(1)).findById(any());
    }

    @DisplayName("""
            Verify updateById() method works correctly and throws EntityNotFoundException
            if Book with such id does not exist
            """)
    @Test
    public void updateById_WithNonExistingBookId_ShouldThrowException() {
        UpdateBookRequestDto updateBookRequestDto = BookUtil.createUpdateToSecondBookRequestDto();
        Book book = BookUtil.createFirstBook();

        doThrow(EntityNotFoundException.class).when(bookRepository).findById(book.getId());

        assertThrows(EntityNotFoundException.class, () -> bookService.updateById(book.getId(),
                updateBookRequestDto));
        verify(bookRepository, times(1)).findById(any());
        verify(bookMapper, never()).updateBookFromRequestDto(any(), any());
        verify(bookRepository, never()).save(any());
    }

    @DisplayName("""
            Verify getById() method works correctly and returns correct BookDto with valid id
            """)
    @Test
    public void getById_WithValidInputData_ShouldReturnCorrectBookDto() {
        Book book = BookUtil.createFirstBook();
        BookDto bookDto = BookUtil.createFirstBookDto();

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.getById(book.getId());

        assertEquals(bookDto, result);
        verify(bookRepository, times(1)).findById(any());
        verify(bookMapper, times(1)).toDto(any());
    }

    @DisplayName("""
            Verify getById() works correctly and throws EntityNotFoundException with non existing id
            """)
    @Test
    public void getById_WithNonExistingBookId_ShouldThrowException() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.getById(bookId));
        verify(bookRepository, times(1)).findById(any());
        verify(bookMapper, never()).toDto(any());
        verify(bookRepository, never()).save(any());
    }

    @DisplayName("""
            Verify findAll() method works correctly and returns correct list of BookDto
            with valid pageable input
            """)
    @Test
    public void findAll_WithTwoBooksInDatabase_ShouldReturnListWithTwoBookDtos() {
        Book firstBook = BookUtil.createFirstBook();
        Book secondBook = BookUtil.createSecondBook();
        BookDto firstBookDto = BookUtil.createFirstBookDto();
        BookDto secondBookDto = BookUtil.createSecondBookDto();

        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> bookPage = new PageImpl<>(List.of(firstBook, secondBook));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
        when(bookMapper.toDto(secondBook)).thenReturn(secondBookDto);

        List<BookDto> expected = List.of(firstBookDto, secondBookDto);

        List<BookDto> result = bookService.findAll(pageable);
        assertEquals(expected, result);
        verify(bookRepository, times(1)).findAll(any(Pageable.class));
        verify(bookMapper, times(expected.size())).toDto(any());
    }

    @DisplayName("""
            Verify findAll() method works correctly and returns empty list with empty database
            """)
    @Test
    public void findAll_WithNoBooks_ShouldReturnEmptyList() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> emptyPage = new PageImpl<>(List.of());

        when(bookRepository.findAll(pageable)).thenReturn(emptyPage);

        List<BookDto> expected = List.of();

        List<BookDto> result = bookService.findAll(pageable);

        assertEquals(expected, result);
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, never()).toDto(any(Book.class));
    }

    @DisplayName("""
            Verify findByCategory() works correctly and method returns correct list with BookDtos
            with valid category input
            """)
    @Test
    public void findByCategory_WithTwoRequiredBooks_ShouldReturnListWithTwoBookDtos() {
        Book firstBook = BookUtil.createFirstBook();
        Book secondBook = BookUtil.createSecondBook();
        BookDto firstBookDto = BookUtil.createFirstBookDto();
        BookDto secondBookDto = BookUtil.createSecondBookDto();

        Category category = CategoryUtil.createFirstCategory();

        List<Book> booksWithSearchedCategory = List.of(firstBook, secondBook);

        when(bookRepository.findAllByCategoriesId(category.getId()))
                .thenReturn(booksWithSearchedCategory);
        when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
        when(bookMapper.toDto(secondBook)).thenReturn(secondBookDto);

        List<BookDto> expected = List.of(firstBookDto, secondBookDto);

        List<BookDto> actual = bookService.findByCategory(category.getId());

        assertEquals(expected, actual);
        verify(bookRepository, times(1))
                .findAllByCategoriesId(category.getId());
        verify(bookMapper, times(expected.size())).toDto(any());
    }

    @DisplayName("""
            Verify findByCategory() method works correctly and returns empty list if there are no
            books with searched category
            """)
    @Test
    public void findByCategory_WithNoBooksWithSearchedCategory_ShouldReturnEmptyList() {
        Category searchedCategory = new Category();
        searchedCategory.setId(1L);

        List<Book> noBooksList = new ArrayList<>();

        when(bookRepository.findAllByCategoriesId(searchedCategory.getId()))
                .thenReturn(noBooksList);

        List<BookDto> expected = new ArrayList<>();

        List<BookDto> actual = bookService.findByCategory(searchedCategory.getId());

        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findAllByCategoriesId(any());
        verify(bookMapper, never()).toDto(any());
    }

    @DisplayName("""
            Verify search() method works correctly and returns list with two book dtos with valid
            bookSearchParams
            """)
    @Test
    public void search_WithTwoSearchParamsAndValidBooks_ShouldReturnListWithValidBookDtos() {
        Book firstBook = BookUtil.createFirstBook();
        Book secondBook = BookUtil.createSecondBook();
        BookDto firstBookDto = BookUtil.createFirstBookDto();
        BookDto secondBookDto = BookUtil.createSecondBookDto();

        BookSearchParameters bookSearchParameters =
                new BookSearchParameters("Book", "Author");
        Specification<Book> bookSpecification = mock(Specification.class);

        List<Book> bookList = List.of(firstBook, secondBook);

        when(bookSpecificationBuilder.build(bookSearchParameters)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification)).thenReturn(bookList);
        when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
        when(bookMapper.toDto(secondBook)).thenReturn(secondBookDto);

        List<BookDto> expected = List.of(firstBookDto, secondBookDto);

        List<BookDto> actual = bookService.search(bookSearchParameters);

        assertEquals(expected, actual);
        verify(bookSpecificationBuilder, times(1)).build(any());
        verify(bookRepository, times(1)).findAll(any(Specification.class));
        verify(bookMapper, times(expected.size())).toDto(any());
    }

    @DisplayName("""
            Verify search() method works correctly and returns empty list when there is no books
            matches search params
            """)
    @Test
    public void search_WithNoRequiredBooks_ShouldReturnEmptyList() {
        BookSearchParameters bookSearchParameters =
                new BookSearchParameters("Book", "Author");
        Specification<Book> bookSpecification = mock(Specification.class);

        when(bookSpecificationBuilder.build(bookSearchParameters)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification)).thenReturn(List.of());

        List<BookDto> expected = new ArrayList<>();

        List<BookDto> actual = bookService.search(bookSearchParameters);

        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findAll(any(Specification.class));
        verify(bookSpecificationBuilder, times(1)).build(any());
        verify(bookMapper, never()).toDto(any());
    }
}

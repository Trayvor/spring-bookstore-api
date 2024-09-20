package org.example.bookstore.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.dto.book.UpdateBookRequestDto;
import org.example.bookstore.model.Book;

public class BookUtil {
    public static Book createFirstBook() {
        return new Book().setId(1L).setTitle("Book One").setAuthor("Author One")
                .setIsbn("978-3-16-148410-0").setPrice(new BigDecimal("20"))
                .setDescription("Fiction book description").setCoverImage("cover1.jpg")
                .setCategories(Set.of(CategoryUtil.createFirstCategory()));
    }

    public static Book createSecondBook() {
        return new Book().setId(2L).setTitle("Book Two").setAuthor("Author Two")
                .setIsbn("978-1-4028-9462-6").setPrice(new BigDecimal("25"))
                .setDescription("Non-Fiction book description").setCoverImage("cover2.jpg")
                .setCategories(Set.of(CategoryUtil.createFirstCategory()));
    }

    public static Book createThirdBook() {
        return new Book().setId(3L).setTitle("Book Three").setAuthor("Author Three")
                .setIsbn("978-0-545-01022-1").setPrice(new BigDecimal("15"))
                .setDescription("Another Fiction book").setCoverImage("cover3.jpg")
                .setCategories(Set.of(CategoryUtil.createSecondCategory()));
    }

    public static Book createFourthBook() {
        return new Book().setId(4L).setTitle("Book Four").setAuthor("Author Four")
                .setIsbn("978-0-7432-7356-5").setPrice(new BigDecimal("30"))
                .setDescription("Another Non-Fiction book").setCoverImage("cover4.jpg")
                .setCategories(Set.of(CategoryUtil.createSecondCategory()));
    }

    public static BookDto createFirstBookDto() {
        return new BookDto().setId(1L).setTitle("Book One").setAuthor("Author One")
                .setIsbn("978-3-16-148410-0").setPrice(new BigDecimal("20"))
                .setDescription("Fiction book description").setCoverImage("cover1.jpg")
                .setCategoriesIds(List.of(CategoryUtil.createFirstCategory().getId()));
    }

    public static BookDto createSecondBookDto() {
        return new BookDto().setId(2L).setTitle("Book Two").setAuthor("Author Two")
                .setIsbn("978-1-4028-9462-6").setPrice(new BigDecimal("25"))
                .setDescription("Non-Fiction book description").setCoverImage("cover2.jpg")
                .setCategoriesIds(List.of(CategoryUtil.createFirstCategory().getId()));
    }

    public static BookDto createThirdBookDto() {
        return new BookDto().setId(3L).setTitle("Book Three").setAuthor("Author Three")
                .setIsbn("978-0-545-01022-1").setPrice(new BigDecimal("15"))
                .setDescription("Another Fiction book").setCoverImage("cover3.jpg")
                .setCategoriesIds(List.of(CategoryUtil.createSecondCategory().getId()));
    }

    public static BookDto createFourthBookDto() {
        return new BookDto().setId(4L).setTitle("Book Four").setAuthor("Author Four")
                .setIsbn("978-0-7432-7356-5").setPrice(new BigDecimal("30"))
                .setDescription("Another Non-Fiction book").setCoverImage("cover4.jpg")
                .setCategoriesIds(List.of(CategoryUtil.createSecondCategory().getId()));
    }

    public static CreateBookRequestDto createCreateFirstBookBookRequestDto() {
        return new CreateBookRequestDto().setTitle("Book One").setAuthor("Author One")
                .setIsbn("978-3-16-148410-0").setPrice(new BigDecimal("20"))
                .setDescription("Fiction book description").setCoverImage("cover1.jpg")
                .setCategoriesIds(List.of(CategoryUtil.createFirstCategory().getId()));
    }

    public static UpdateBookRequestDto createUpdateToSecondBookRequestDto() {
        return new UpdateBookRequestDto().setTitle("Book Two").setAuthor("Author Two")
                .setIsbn("978-1-4028-9462-6").setPrice(new BigDecimal("25"))
                .setDescription("Non-Fiction book description").setCoverImage("cover2.jpg")
                .setCategoriesIds(List.of(CategoryUtil.createFirstCategory().getId()));
    }

    public static List<BookDto> createFourBookDtoList() {
        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(createFirstBookDto());
        bookDtoList.add(createSecondBookDto());
        bookDtoList.add(createThirdBookDto());
        bookDtoList.add(createFourthBookDto());
        return bookDtoList;
    }
}

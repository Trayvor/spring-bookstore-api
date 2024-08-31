package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.dto.book.UpdateBookRequestDto;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBookFromRequestDto(UpdateBookRequestDto requestDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        for (Category category : book.getCategories()) {
            bookDto.getCategoriesIds().add(category.getId());
        }
    }

    @AfterMapping
    default void setCategory(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        for (Long id : requestDto.getCategoriesIds()) {
            Category category = new Category();
            category.setId(id);
            book.getCategories().add(category);
        }
    }

    @AfterMapping
    default void setCategory(@MappingTarget Book book, UpdateBookRequestDto requestDto) {
        for (Long id : requestDto.getCategoriesIds()) {
            Category category = new Category();
            category.setId(id);
            book.getCategories().add(category);
        }
    }
}

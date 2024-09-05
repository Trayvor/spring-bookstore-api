package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.cart.item.CartItemDto;
import org.example.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.example.bookstore.dto.cart.item.UpdateCartItemRequestDto;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItem toModel(CreateCartItemRequestDto createCartItemRequestDto);

    CartItem toModel(CartItemDto cartItemDto);

    CartItemDto toDto(CartItem cartItem);

    void updateCartItemFromRequestDto(UpdateCartItemRequestDto updateCartItemRequestDto,
                                      @MappingTarget CartItem cartItem);

    @AfterMapping
    default void setModelValues(@MappingTarget CartItem cartItem,
                             CreateCartItemRequestDto createCartItemRequestDto) {
        Book book = new Book();
        book.setId(createCartItemRequestDto.getBookId());
        cartItem.setBook(book);
    }

    @AfterMapping
    default void setDtoValues(@MappingTarget CartItemDto cartItemDto, CartItem cartItem) {
        cartItemDto.setBookId(cartItem.getBook().getId());
        cartItemDto.setBookTitle(cartItem.getBook().getTitle());
    }
}

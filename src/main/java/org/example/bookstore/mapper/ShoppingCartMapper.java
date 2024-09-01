package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.cart.item.CartItemDto;
import org.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCart toModel(ShoppingCartDto shoppingCartDto);

    default ShoppingCartDto toDto(ShoppingCart shoppingCart) {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(shoppingCart.getId());
        shoppingCartDto.setUserId(shoppingCart.getUser().getId());
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setBookTitle(cartItem.getBook().getTitle());
            cartItemDto.setQuantity(cartItem.getQuantity());
            cartItemDto.setBookId(cartItem.getBook().getId());
            cartItemDto.setId(cartItem.getId());
            shoppingCartDto.getCartItems().add(cartItemDto);
        }
        return shoppingCartDto;
    }

    @AfterMapping
    default void setEntityValues(ShoppingCartDto shoppingCartDto,
                             @MappingTarget ShoppingCart shoppingCart) {
        User user = new User();
        user.setId(shoppingCartDto.getUserId());
        shoppingCart.setUser(user);
        for (CartItemDto cartItemDto : shoppingCartDto.getCartItems()) {
            CartItem cartItem = new CartItem();
            cartItem.setId(cartItemDto.getId());
            shoppingCart.getCartItems().add(cartItem);
        }
    }
}

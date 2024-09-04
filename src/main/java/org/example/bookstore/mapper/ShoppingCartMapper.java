package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import org.example.bookstore.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @AfterMapping
    default void setDtoValues(ShoppingCart shoppingCart,
                              @MappingTarget ShoppingCartDto shoppingCartDto) {
        CartItemMapper cartItemMapper = Mappers.getMapper(CartItemMapper.class);
        shoppingCartDto.setUserId(shoppingCart.getUser().getId());
        shoppingCart.getCartItems()
                .forEach(cartItem ->
                        shoppingCartDto.getCartItems().add(cartItemMapper.toDto(cartItem))
                );
    }
}

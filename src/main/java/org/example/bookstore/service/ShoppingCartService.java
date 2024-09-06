package org.example.bookstore.service;

import org.example.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.example.bookstore.dto.cart.item.UpdateCartItemRequestDto;
import org.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import org.example.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto addItemToShoppingCart(CreateCartItemRequestDto createCartItemRequestDto,
                                          Long userId);

    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto updateCartItem(Long itemId, UpdateCartItemRequestDto updateCartItemRequestDto,
                                   Long userId);

    void deleteCartItem(Long itemId, Long userId);

    void createShoppingCart(User user);
}

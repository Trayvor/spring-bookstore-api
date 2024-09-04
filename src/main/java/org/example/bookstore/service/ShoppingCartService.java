package org.example.bookstore.service;

import org.example.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.example.bookstore.dto.cart.item.UpdateCartItemRequestDto;
import org.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import org.example.bookstore.model.User;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartDto addItemToShoppingCart(CreateCartItemRequestDto createCartItemRequestDto,
                                          Authentication authentication);

    ShoppingCartDto getShoppingCart(Authentication authentication);

    ShoppingCartDto updateCartItem(Long id, UpdateCartItemRequestDto updateCartItemRequestDto,
                                   Authentication authentication);

    void deleteCartItem(Long id, Authentication authentication);

    void createShoppingCartForUser(User user);
}

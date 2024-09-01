package org.example.bookstore.service;

import org.example.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.example.bookstore.dto.shopping.cart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto addItemToShoppingCart(CreateCartItemRequestDto createCartItemRequestDto);

    ShoppingCartDto getShoppingCart();
}

package org.example.bookstore.service;

import org.example.bookstore.dto.cart.item.UpdateCartItemRequestDto;
import org.example.bookstore.dto.cart.item.UpdateCartItemResponseDto;

public interface CartItemService {
    UpdateCartItemResponseDto updateCartItem(Long id,
                                             UpdateCartItemRequestDto updateCartItemRequestDto);

    void deleteCartItem(Long id);
}

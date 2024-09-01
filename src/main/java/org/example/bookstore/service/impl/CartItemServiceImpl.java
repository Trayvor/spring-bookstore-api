package org.example.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.cart.item.UpdateCartItemRequestDto;
import org.example.bookstore.dto.cart.item.UpdateCartItemResponseDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.CartItemMapper;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.repository.cart.item.CartItemRepository;
import org.example.bookstore.service.CartItemService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public UpdateCartItemResponseDto updateCartItem(Long id,
                                                    UpdateCartItemRequestDto cartItemRequestDto) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find cart item with id " + id)
        );
        cartItemMapper.updateCartItemFromRequestDto(cartItemRequestDto, cartItem);
        cartItemRepository.save(cartItem);
        return cartItemMapper.toUpdateResponseDto(cartItem);
    }

    @Override
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }
}

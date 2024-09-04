package org.example.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.example.bookstore.dto.cart.item.UpdateCartItemRequestDto;
import org.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.CartItemMapper;
import org.example.bookstore.mapper.ShoppingCartMapper;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.cart.item.CartItemRepository;
import org.example.bookstore.repository.shopping.cart.ShoppingCartRepository;
import org.example.bookstore.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto addItemToShoppingCart(CreateCartItemRequestDto cartItemRequestDto,
                                                 Authentication authentication) {
        ShoppingCart shoppingCart =
                shoppingCartRepository.findByUserEmail(authentication.getName()).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can`t find shopping cart for current user with"
                                        + " email " + authentication.getName())
                );
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
        return getShoppingCart(authentication);
    }

    @Override
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        return shoppingCartMapper.toDto(shoppingCartRepository
                .findByUserEmail(authentication.getName()).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can`t find shopping cart for current user with"
                                        + " email " + authentication.getName())
                )
        );
    }

    @Override
    public ShoppingCartDto updateCartItem(Long id,
                                          UpdateCartItemRequestDto cartItemRequestDto,
                                          Authentication authentication) {
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartUserEmail(id, authentication.getName()).orElseThrow(
                        () -> new EntityNotFoundException("Can`t find cart item with id " + id
                                + ", because it does not exists or not belongs to user wit email "
                                + authentication.getName())
                );
        cartItemMapper.updateCartItemFromRequestDto(cartItemRequestDto, cartItem);
        cartItemRepository.save(cartItem);
        return getShoppingCart(authentication);
    }

    @Override
    public void deleteCartItem(Long id, Authentication authentication) {
        if (!cartItemRepository.existsByIdAndShoppingCartUserEmail(id, authentication.getName())) {
            throw new EntityNotFoundException("Can`t find cart item with id " + id
                    + ", because it does not exists or not belongs to user wit email "
                    + authentication.getName());
        }
        cartItemRepository.deleteById(id);
    }

    public void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}

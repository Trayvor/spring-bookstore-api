package org.example.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.cart.item.CreateCartItemRequestDto;
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
import org.example.bookstore.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto addItemToShoppingCart(CreateCartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = getCurrentUserShoppingCart();
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
        return getShoppingCart();
    }

    @Override
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartMapper.toDto(getCurrentUserShoppingCart());
    }

    private ShoppingCart getCurrentUserShoppingCart() {
        User authenticatedUser = userService.getAuthenticatedUser();
        return shoppingCartRepository.findByUserEmail(authenticatedUser.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find shopping cart for current user with"
                        + " email " + authenticatedUser.getEmail())
        );
    }
}

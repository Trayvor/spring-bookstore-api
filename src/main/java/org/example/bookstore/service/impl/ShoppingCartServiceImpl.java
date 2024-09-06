package org.example.bookstore.service.impl;

import java.util.HashSet;
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
                                                 Long userId) {
        ShoppingCart shoppingCart =
                shoppingCartRepository.findByUserId(userId).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can`t find shopping cart for current user with"
                                        + " id " + userId)
                );
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
        return getShoppingCart(userId);
    }

    @Override
    public ShoppingCartDto getShoppingCart(Long userId) {
        return shoppingCartMapper.toDto(shoppingCartRepository
                .findByUserId(userId).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can`t find shopping cart for current user with"
                                        + "id " + userId)
                )
        );
    }

    @Override
    public ShoppingCartDto updateCartItem(Long itemId,
                                          UpdateCartItemRequestDto cartItemRequestDto,
                                          Long userId) {
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartUserId(itemId, userId).orElseThrow(
                        () -> new EntityNotFoundException("Can`t find cart item with id " + itemId
                                + ", because it does not exists or not belongs to user with id "
                                + userId)
                );
        cartItemMapper.updateCartItemFromRequestDto(cartItemRequestDto, cartItem);
        cartItemRepository.save(cartItem);
        return getShoppingCart(userId);
    }

    @Override
    public void deleteCartItem(Long itemId, Long userId) {
        if (!cartItemRepository.existsByIdAndShoppingCartUserId(itemId, userId)) {
            throw new EntityNotFoundException("Can`t find cart item with id " + itemId
                    + ", because it does not exists or not belongs to user with id "
                    + userId);
        }
        cartItemRepository.deleteById(itemId);
    }

    public void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void deleteAllFromShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find shopping cart for current user with"
                        + "id " + userId)
        );
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCartRepository.save(shoppingCart);
    }
}

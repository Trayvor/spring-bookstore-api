package org.example.bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.example.bookstore.dto.cart.item.UpdateCartItemRequestDto;
import org.example.bookstore.dto.cart.item.UpdateCartItemResponseDto;
import org.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import org.example.bookstore.service.CartItemService;
import org.example.bookstore.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartDto getUsersShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartDto addCartItemToShoppingCart(
            @RequestBody CreateCartItemRequestDto createCartItemRequestDto) {
        return shoppingCartService.addItemToShoppingCart(createCartItemRequestDto);
    }

    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UpdateCartItemResponseDto updateCartItemQuantity(@PathVariable Long cartItemId,
                                                            @RequestBody UpdateCartItemRequestDto
                                                          updateCartItemRequestDto) {
        return cartItemService.updateCartItem(cartItemId, updateCartItemRequestDto);
    }

    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        cartItemService.deleteCartItem(cartItemId);
    }
}

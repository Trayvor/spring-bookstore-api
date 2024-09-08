package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.cart.item.CreateCartItemRequestDto;
import org.example.bookstore.dto.cart.item.UpdateCartItemRequestDto;
import org.example.bookstore.dto.shopping.cart.ShoppingCartDto;
import org.example.bookstore.model.User;
import org.example.bookstore.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management",
        description = "Endpoint for mapping shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Find shopping cart for logged in user",
            description = "Returns list of cart items in user`s shopping cart")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartDto getUsersShoppingCart(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @Operation(summary = "Adding cart item",
            description = "Adding cart item in user`s shopping cart")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartDto addCartItemToShoppingCart(
            @RequestBody @Valid CreateCartItemRequestDto createCartItemRequestDto,
            Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return shoppingCartService.addItemToShoppingCart(createCartItemRequestDto, user.getId());
    }

    @Operation(summary = "Updating cart item quantity", description = "Updating cart item quantity")
    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartDto updateCartItemQuantity(@PathVariable Long cartItemId,
                                                  @RequestBody @Valid UpdateCartItemRequestDto
                                                          updateCartItemRequestDto,
                                                  Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return shoppingCartService.updateCartItem(cartItemId,
                updateCartItemRequestDto,
                user.getId());
    }

    @Operation(summary = "Deleting cart item",
            description = "Deleting cart item from user`s shopping cart")
    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteCartItem(@PathVariable Long cartItemId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        shoppingCartService.deleteCartItem(cartItemId, user.getId());
    }

    private User getAuthenticatedUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}

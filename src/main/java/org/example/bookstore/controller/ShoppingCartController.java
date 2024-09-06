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

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Find shopping cart for logged in user",
            description = "Returns list of cart items in user`s shopping cart")
    public ShoppingCartDto getUsersShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Adding cart item",
            description = "Adding cart item in user`s shopping cart")
    public ShoppingCartDto addCartItemToShoppingCart(
            @RequestBody @Valid CreateCartItemRequestDto createCartItemRequestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addItemToShoppingCart(createCartItemRequestDto, user.getId());
    }

    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Updating cart item quantity", description = "Updating cart item quantity")
    public ShoppingCartDto updateCartItemQuantity(@PathVariable Long cartItemId,
                                                  @RequestBody @Valid UpdateCartItemRequestDto
                                                          updateCartItemRequestDto,
                                                  Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItem(cartItemId,
                updateCartItemRequestDto,
                user.getId());
    }

    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Deleting cart item",
            description = "Deleting cart item from user`s shopping cart")
    public void deleteCartItem(@PathVariable Long cartItemId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.deleteCartItem(cartItemId, user.getId());
    }
}

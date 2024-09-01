package org.example.bookstore.dto.cart.item;

import lombok.Data;

@Data
public class UpdateCartItemResponseDto {
    private Long id;
    private Long shoppingCartId;
    private Long bookId;
    private int quantity;
}

package org.example.bookstore.dto.shopping.cart;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.example.bookstore.dto.cart.item.CartItemDto;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> cartItems = new ArrayList<>();
}

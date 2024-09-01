package org.example.bookstore.dto.cart.item;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequestDto {
    @NotNull
    private int quantity;
}

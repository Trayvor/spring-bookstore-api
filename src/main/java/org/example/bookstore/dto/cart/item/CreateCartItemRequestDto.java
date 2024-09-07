package org.example.bookstore.dto.cart.item;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateCartItemRequestDto {
    @Positive
    private long bookId;
    @Positive
    private int quantity;
}

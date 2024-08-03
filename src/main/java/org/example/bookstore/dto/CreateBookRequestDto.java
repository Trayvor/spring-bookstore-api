package org.example.bookstore.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class CreateBookRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String author;
    @ISBN(type = ISBN.Type.ANY)
    private String isbn;
    @NotNull
    @Positive
    private BigDecimal price;
    @NotNull
    private String description;
    private String coverImage;
}

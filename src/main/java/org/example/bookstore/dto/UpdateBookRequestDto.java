package org.example.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class UpdateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @ISBN(type = ISBN.Type.ANY)
    private String isbn;
    @NotNull
    @Positive
    private BigDecimal price;
    @NotBlank
    private String description;
    private String coverImage;
}

package org.example.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.ISBN;

@Accessors(chain = true)
@Data
public class CreateBookRequestDto {
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
    private List<Long> categoriesIds = new ArrayList<>();
}

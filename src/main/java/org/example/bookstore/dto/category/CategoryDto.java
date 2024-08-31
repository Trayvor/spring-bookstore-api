package org.example.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    private String description;
}

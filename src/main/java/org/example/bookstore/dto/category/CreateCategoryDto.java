package org.example.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CreateCategoryDto {
    @NotBlank
    private String name;
    private String description;
}

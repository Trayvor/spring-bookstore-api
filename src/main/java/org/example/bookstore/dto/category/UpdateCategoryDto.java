package org.example.bookstore.dto.category;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class UpdateCategoryDto {
    private String name;
    private String description;
}

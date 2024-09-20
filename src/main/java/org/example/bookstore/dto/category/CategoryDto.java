package org.example.bookstore.dto.category;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
}

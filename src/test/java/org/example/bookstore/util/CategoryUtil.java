package org.example.bookstore.util;

import java.util.ArrayList;
import java.util.List;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CreateCategoryDto;
import org.example.bookstore.dto.category.UpdateCategoryDto;
import org.example.bookstore.model.Category;

public class CategoryUtil {
    public static Category createFirstCategory() {
        return new Category().setId(1L).setName("Fiction")
                .setDescription("Books that contain fictional stories");
    }

    public static Category createSecondCategory() {
        return new Category().setId(2L).setName("Non-Fiction")
                .setDescription("Books that contain factual information");
    }

    public static CategoryDto createFirstCategoryDto() {
        return new CategoryDto().setId(1L).setName("Fiction")
                .setDescription("Books that contain fictional stories");
    }

    public static CategoryDto createSecondCategoryDto() {
        return new CategoryDto().setId(2L).setName("Non-Fiction")
                .setDescription("Books that contain factual information");
    }

    public static CreateCategoryDto createCreateFirstCategoryDto() {
        return new CreateCategoryDto().setName("Fiction")
                .setDescription("Books that contain fictional stories");
    }

    public static UpdateCategoryDto createUpdateCategoryDto() {
        return new UpdateCategoryDto().setName("Fiction")
                .setDescription("Books that contain fictional stories");
    }

    public static List<CategoryDto> createTwoCategoriesDtoList() {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryDtoList.add(createFirstCategoryDto());
        categoryDtoList.add(createSecondCategoryDto());
        return categoryDtoList;
    }
}

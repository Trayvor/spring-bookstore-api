package org.example.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CreateCategoryDto;
import org.example.bookstore.dto.category.UpdateCategoryDto;
import org.example.bookstore.util.BookUtil;
import org.example.bookstore.util.CategoryUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;

    private static final String BASIC_URL_ENDPOINT = "/categories";

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
    }

    @AfterEach
    void afterEach(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/books-categories/delete-books-categories-dependencies.sql"
                    )
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/category/delete-categories-from-categories-table.sql"
                    )
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/book/delete-books-from-books-table.sql"
                    )
            );
        }
    }

    @DisplayName("""
            Verify getAll endpoint works correctly and returns list with categories
            """)
    @Sql(scripts = "classpath:database/category/add-two-categories-to-database.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void getAll_GivenCategoriesInCatalog_ShouldReturnAllCategories() throws Exception {
        List<CategoryDto> expected = CategoryUtil.createTwoCategoriesDtoList();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASIC_URL_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        CategoryDto[] actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), CategoryDto[].class);
        Assertions.assertEquals(expected.size(), actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @DisplayName("""
            Verify getCategoryById endpoint works correctly and returns category
            """)
    @Sql(scripts = "classpath:database/category/add-two-categories-to-database.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void getCategoryById_WithValidId_ShouldReturnCategory() throws Exception {
        CategoryDto expected = CategoryUtil.createFirstCategoryDto();
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASIC_URL_ENDPOINT + "/" + expected.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), CategoryDto.class);
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("""
            Verify createCategory endpoint works correctly and returns saved category
            """)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void createCategory_ValidCreateCategoryDto_ShouldReturnCategoryDto() throws Exception {
        CreateCategoryDto createCategoryRequestDto = CategoryUtil.createCreateFirstCategoryDto();
        CategoryDto expected = CategoryUtil.createFirstCategoryDto();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post(BASIC_URL_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createCategoryRequestDto))
                ).andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @DisplayName("""
            Verify updateCategory works correctly and returns updated category
            """)
    @Sql(scripts = "classpath:database/category/add-one-category-to-database.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void updateCategory_WithValidIdAndUpdateDto_ShouldReturnCategoryDto() throws Exception {
        CategoryDto expected = CategoryUtil.createSecondCategoryDto();
        UpdateCategoryDto updateCategoryRequestDto = CategoryUtil.createUpdateCategoryDto();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.put(BASIC_URL_ENDPOINT + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateCategoryRequestDto))
                ).andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @DisplayName("""
            Verify getBooksByCategory works correctly and returns list of books
            """)
    @Sql(scripts = {
            "classpath:database/book/add-four-books-to-database.sql",
            "classpath:database/category/add-two-categories-to-database.sql",
            "classpath:database/books-categories/add-books-categories-dependencies.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void getBooksByCategoryId_WithValidCategoryId_ShouldReturnBookList() throws Exception {
        List<BookDto> expected = List.of(BookUtil.createFirstBookDto(),
                BookUtil.createSecondBookDto());

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get(BASIC_URL_ENDPOINT + "/1/books")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), BookDto[].class);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList(),
                "id");
    }

    @DisplayName("""
            Verify delete endpoint works correctly with success status code 200
            """)
    @Sql(scripts = "classpath:database/category/add-one-category-to-database.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void delete_WithValidId_ShouldReturnSuccess() throws Exception {
        CategoryDto categoryToDelete = CategoryUtil.createFirstCategoryDto();
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.delete(BASIC_URL_ENDPOINT + "/"
                                        + categoryToDelete.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
    }
}

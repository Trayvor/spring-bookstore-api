package org.example.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.dto.book.UpdateBookRequestDto;
import org.example.bookstore.util.BookUtil;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;

    private static final String SEARCH_PARAM_TITLE = "title";
    private static final String SEARCH_PARAM_AUTHOR = "author";
    private static final String FIRST_BOOK_TITLE = "Book One";
    private static final String FIRST_BOOK_AUTHOR = "Author One";
    private static final String BASIC_URL_ENDPOINT = "/books";

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
            Verify findAll endpoint works correctly and returns list of all books
            """)
    @Sql(scripts = {
            "classpath:database/book/add-four-books-to-database.sql",
            "classpath:database/category/add-two-categories-to-database.sql",
            "classpath:database/books-categories/add-books-categories-dependencies.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void findAll_GivenBooksInCatalog_ShouldReturnAllBooks() throws Exception {
        List<BookDto> expected = BookUtil.createFourBookDtoList();

        MvcResult result = mockMvc.perform(
                        get(BASIC_URL_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(expected.size(), actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @DisplayName("""
            Verify getBookById endpoint works correctly and returns correct book
            """)
    @Sql(scripts = {
            "classpath:database/book/add-four-books-to-database.sql",
            "classpath:database/category/add-two-categories-to-database.sql",
            "classpath:database/books-categories/add-books-categories-dependencies.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void getBookById_ValidBookId_ShouldReturnBook() throws Exception {
        BookDto expected = BookUtil.createFirstBookDto();
        MvcResult result = mockMvc.perform(
                        get(BASIC_URL_ENDPOINT + "/" + expected.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), BookDto.class);
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("""
            Verify createBook endpoint works correctly and returns created book dto
            """)
    @Sql(scripts = "classpath:database/category/add-two-categories-to-database.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void createBook_ValidCreateBookDto_ShouldReturnBookDto() throws Exception {
        CreateBookRequestDto createBookRequestDto = BookUtil.createCreateFirstBookBookRequestDto();
        BookDto expected = BookUtil.createFirstBookDto();

        MvcResult result = mockMvc.perform(
                        post(BASIC_URL_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createBookRequestDto))
                ).andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @DisplayName("""
            Verify updateBook endpoint works correctly and returns updated book dto
            """)
    @Sql(scripts = {
            "classpath:database/category/add-two-categories-to-database.sql",
            "classpath:database/book/add-one-book-to-database.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void updateBook_WithValidIdAndUpdateDto_ShouldReturnBookDto() throws Exception {
        BookDto expected = BookUtil.createSecondBookDto();
        UpdateBookRequestDto updateBookRequestDto = BookUtil.createUpdateToSecondBookRequestDto();

        MvcResult result = mockMvc.perform(
                        put(BASIC_URL_ENDPOINT + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateBookRequestDto))
                ).andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @DisplayName("""
            Verify search endpoint works correctly and returns correct book
            """)
    @Sql(scripts = {
            "classpath:database/book/add-four-books-to-database.sql",
            "classpath:database/category/add-two-categories-to-database.sql",
            "classpath:database/books-categories/add-books-categories-dependencies.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void search_WithValidFirstBookSearchParams_ShouldReturnFirstBook() throws Exception {
        List<BookDto> expected = List.of(BookUtil.createFirstBookDto());

        MvcResult result = mockMvc.perform(
                        get(BASIC_URL_ENDPOINT + "/search")
                                .param(SEARCH_PARAM_TITLE, FIRST_BOOK_TITLE)
                                .param(SEARCH_PARAM_AUTHOR, FIRST_BOOK_AUTHOR)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(expected.size(), actual.length);
        EqualsBuilder.reflectionEquals(expected.get(0), actual[0], "id");
    }

    @DisplayName("""
            Verify delete endpoint works correctly with Ok status code 200
            """)
    @Sql(scripts = "classpath:database/category/add-two-categories-to-database.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void delete_ExistingBook_ShouldReturnSuccess() throws Exception {
        BookDto bookToDelete = BookUtil.createFirstBookDto();
        MvcResult result = mockMvc.perform(
                        delete(BASIC_URL_ENDPOINT + "/" + bookToDelete.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
    }
}

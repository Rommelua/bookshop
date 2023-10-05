package com.bookshop.controller;

import com.bookshop.dto.book.BookDto;
import com.bookshop.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerTest {
    private static final long BOOK_ID_FOR_CRUD_OPERATIONS = 6L;
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeAll
    @SneakyThrows
    static void setup(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) {
        teardown(dataSource);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/add-three-default-books.sql")
            );
        }
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/remove-all-books-and-categories.sql")
            );
        }
    }

    @Order(1)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get all books")
    public void getAll_GivenBooksInCatalog_ShouldReturnAllBooks() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto[] books = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto[].class);
        assertThat(books.length).isEqualTo(3); // assert that all 3 books are returned
        List<Long> bookIds = Arrays.stream(books)
                .map(BookDto::getId)
                .collect(Collectors.toList());
        // assert that the books returned have IDs 1, 2, and 3
        assertThat(bookIds).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get book details by id")
    public void getBookById_GivenExistingBookId_ShouldReturnBookDetails() throws Exception {
        // Given: A book with ID 1 exists in the database
        Long bookId = 1L;

        // When
        MvcResult result = mockMvc.perform(get("/books/" + bookId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto book = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        assertThat(book.getId()).isEqualTo(bookId);
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Search all books by author and title and isbn")
    public void searchBooks_GivenAuthorAndTitleAndLowPrice_ShouldReturnOneMatchingBook()
            throws Exception {
        //Given
        String title = "Dune";
        String author = "Frank Herbert";
        String minPrice = "12";

        // When
        MvcResult result = mockMvc.perform(get("/books/search")
                .param("titleContains", title)
                .param("authorIn", author)
                .param("priceMin", minPrice)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto[] books = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto[].class);
        assertThat(books.length).isEqualTo(1);
        BookDto book = books[0];
        assertThat(book.getTitle()).isEqualTo(title);
        assertThat(book.getAuthor()).isEqualTo(author);
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/add-more-books-for-search.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/remove-more-books-for-search.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Search all books by author and title")
    public void searchBooks_GivenAuthorAndTitle_ShouldReturnAllMatchingBooks() throws Exception {
        // Given
        String title = "Dune";
        String author = "Frank Herbert";
        List<String> expectedIsbnList = Arrays.asList("9780441013593", "9780441104024");

        // When
        MvcResult result = mockMvc.perform(get("/books/search")
                .param("titleContains", title)
                .param("authorIn", author))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto[] books = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto[].class);
        assertThat(books.length).isEqualTo(2);

        // Expecting the books have the correct title and author
        for (BookDto book : books) {
            assertThat(book.getTitle()).containsIgnoringCase(title);
            assertThat(book.getAuthor()).isEqualTo(author);
        }

        // Checking the ISBNs of the books
        List<String> actualIsbnList = Arrays.stream(books)
                .map(BookDto::getIsbn)
                .collect(Collectors.toList());
        assertThat(actualIsbnList).containsAll(expectedIsbnList);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/add-more-books-for-search.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/remove-more-books-for-search.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Search all books by author")
    public void searchBooks_GivenAuthorOnly_ShouldReturnAllMatchingBooks() throws Exception {
        // Given
        String author = "Frank Herbert";
        int expectedBookCount = 3;
        List<String> expectedTitles = Arrays.asList("Dune", "The Godmakers", "Children of Dune");

        // When
        MvcResult result = mockMvc.perform(get("/books/search")
                .param("authorIn", author))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto[] books = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto[].class);
        // Expecting to receive 3 books with the author "Frank Herbert"
        assertThat(books.length).isEqualTo(expectedBookCount);

        // Checking the author of the books
        for (BookDto book : books) {
            assertThat(book.getAuthor()).isEqualTo(author);
        }

        // Checking the titles of the books
        List<String> actualTitles = Arrays.stream(books)
                .map(BookDto::getTitle)
                .collect(Collectors.toList());
        assertThat(actualTitles).containsAll(expectedTitles);
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a new book")
    void createBook_ValidBookDto_Success() throws Exception {
        // Given
        BookDto bookDto = new BookDto()
                .setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setIsbn("9780743273565")
                .setPrice(BigDecimal.valueOf(9.99))
                .setDescription("A classic novel set in the 1920s, portraying the glittering "
                        + "and decadent world of the wealthy elite.")
                .setCoverImage("great-gatsby-cover.jpg")
                .setCategoryIds(Set.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(bookDto);

        // When
        MvcResult result = mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        BookDto createdBookDto = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);

        assertAll(
                "Created book",
                () -> assertNotNull(createdBookDto.getId()),
                () -> assertEquals(BOOK_ID_FOR_CRUD_OPERATIONS, createdBookDto.getId()),
                () -> assertEquals(bookDto.getTitle(), createdBookDto.getTitle()),
                () -> assertEquals(bookDto.getAuthor(), createdBookDto.getAuthor()),
                () -> assertEquals(bookDto.getIsbn(), createdBookDto.getIsbn()),
                () -> assertEquals(bookDto.getPrice(), createdBookDto.getPrice()),
                () -> assertEquals(bookDto.getDescription(), createdBookDto.getDescription()),
                () -> assertEquals(bookDto.getCoverImage(), createdBookDto.getCoverImage()),
                () -> assertEquals(bookDto.getCategoryIds(), createdBookDto.getCategoryIds())
        );
    }

    @Test
    @Order(6)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update book")
    void updateBook_ValidBookDto_Success() throws Exception {
        // Given
        Long bookId = BOOK_ID_FOR_CRUD_OPERATIONS;
        CreateBookRequestDto updatedBookDto = new CreateBookRequestDto()
                .setTitle("To Kill a Mockingbird")
                .setAuthor("Harper Lee")
                .setIsbn("9780061120084")
                .setPrice(BigDecimal.valueOf(11.99))
                .setDescription("A powerful story set in the 1930s that addresses racial injustice "
                        + "and loss of innocence.")
                .setCoverImage("mockingbird-cover.jpg")
                .setCategoryIds(Set.of(2L));

        String jsonRequest = objectMapper.writeValueAsString(updatedBookDto);

        // When
        MvcResult result = mockMvc.perform(put("/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto updatedBookResponse = objectMapper
                .readValue(result.getResponse().getContentAsString(), BookDto.class);
        assertEquals(bookId, updatedBookResponse.getId());
        assertEquals(updatedBookDto.getTitle(), updatedBookResponse.getTitle());
        assertEquals(updatedBookDto.getAuthor(), updatedBookResponse.getAuthor());
        assertEquals(updatedBookDto.getIsbn(), updatedBookResponse.getIsbn());
        assertEquals(updatedBookDto.getPrice(), updatedBookResponse.getPrice());
        assertEquals(updatedBookDto.getDescription(), updatedBookResponse.getDescription());
        assertEquals(updatedBookDto.getCoverImage(), updatedBookResponse.getCoverImage());
        assertEquals(updatedBookDto.getCategoryIds(), updatedBookResponse.getCategoryIds());
    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete book by id")
    void deleteBook_Success() throws Exception {
        // Given
        Long bookId = BOOK_ID_FOR_CRUD_OPERATIONS;

        // When
        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNoContent());

        // Then
        mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isNotFound());
    }
}

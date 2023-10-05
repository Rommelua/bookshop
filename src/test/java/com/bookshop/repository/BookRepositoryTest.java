package com.bookshop.repository;

import com.bookshop.model.Book;
import com.bookshop.model.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void findAllByCategoryId_WithOneCategory_ShouldReturnListOfBooks() {
        // Given
        Category category = new Category();
        category.setName("Science Fiction");
        category.setDescription("Fiction in which advanced technology and science are key elements of the plot");
        categoryRepository.save(category);

        Book book1 = new Book();
        String title1 = "Book 1";
        book1.setTitle(title1);
        book1.setAuthor("Author 1");
        book1.setIsbn("ISBN 1");
        book1.setPrice(BigDecimal.valueOf(9.99));
        book1.setCategories(Set.of(category));
        bookRepository.save(book1);

        Book book2 = new Book();
        String title2 = "Book 2";
        book2.setTitle(title2);
        book2.setAuthor("Author 2");
        book2.setIsbn("ISBN 2");
        book2.setPrice(BigDecimal.valueOf(19.99));
        book2.setCategories(Set.of(category));
        bookRepository.save(book2);

        Pageable pageable = Pageable.ofSize(10);

        // When
        List<Book> actual = bookRepository.findAllByCategoryId(category.getId(), pageable);

        // Then
        assertEquals(2, actual.size());

        List<String> expectedTitles = List.of(title1, title2);
        List<String> actualTitles = actual.stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
        assertTrue(actualTitles.containsAll(expectedTitles));
    }

    @Test
    public void findAllByCategoryId_WithTwoCategories_ShouldReturnListOfBooks() {
        // Given
        Category scienceCategory = new Category();
        scienceCategory.setName("Science Fiction");
        scienceCategory.setDescription("Fiction in which advanced technology and science are key elements of the plot");
        categoryRepository.save(scienceCategory);

        Category poemCategory = new Category();
        poemCategory.setName("Poem");
        poemCategory.setDescription("Poem category");
        categoryRepository.save(poemCategory);

        Book book1 = new Book();
        String title1 = "Book 1";
        book1.setTitle(title1);
        book1.setAuthor("Author 1");
        book1.setIsbn("ISBN 1");
        book1.setPrice(BigDecimal.valueOf(9.99));
        book1.setCategories(Set.of(scienceCategory));
        bookRepository.save(book1);

        Book book2 = new Book();
        String title2 = "Book 2";
        book2.setTitle(title2);
        book2.setAuthor("Author 2");
        book2.setIsbn("ISBN 2");
        book2.setPrice(BigDecimal.valueOf(19.99));
        book2.setCategories(Set.of(poemCategory));
        bookRepository.save(book2);

        Pageable pageable = Pageable.ofSize(10);

        // When
        List<Book> actualBooksWithScienceCategory = bookRepository.findAllByCategoryId(scienceCategory.getId(), pageable);
        List<Book> actualBooksWithPoemCategory = bookRepository.findAllByCategoryId(poemCategory.getId(), pageable);

        // Then
        assertFalse(actualBooksWithScienceCategory.isEmpty());
        assertEquals(1, actualBooksWithScienceCategory.size());
        assertFalse(actualBooksWithPoemCategory.isEmpty());
        assertEquals(1, actualBooksWithPoemCategory.size());

        List<String> expectedScienceTitles = List.of(title1);
        List<String> actualScienceTitles = actualBooksWithScienceCategory.stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
        assertTrue(actualScienceTitles.containsAll(expectedScienceTitles));

        List<String> expectedPoemTitles = List.of(title2);
        List<String> actualPoemTitles = actualBooksWithPoemCategory.stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
        assertTrue(actualPoemTitles.containsAll(expectedPoemTitles));
    }
}

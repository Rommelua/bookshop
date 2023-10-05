package com.bookshop.service;

import com.bookshop.dto.book.BookDto;
import com.bookshop.dto.book.BookDtoWithoutCategoryIds;
import com.bookshop.dto.book.CreateBookRequestDto;
import com.bookshop.mapper.BookMapper;
import com.bookshop.model.Book;
import com.bookshop.model.Category;
import com.bookshop.repository.BookRepository;
import com.bookshop.repository.CategoryRepository;
import com.bookshop.repository.specification.book.BookSpecificationManager;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private  BookSpecificationManager specificationManager;

    @Test
    @DisplayName("Verify findAll() works")
    public void findAll_ShouldReturnAllBooks() {
        // Given
        List<Book> books = getTestBookList();
        List<BookDto> expected = getTestBookDtoList();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(books.get(0))).thenReturn(expected.get(0));
        when(bookMapper.toDto(books.get(1))).thenReturn(expected.get(1));

        // When
        List<BookDto> actual = bookService.findAll(pageable);

        // Then
        assertThat(actual).hasSize(2);
        assertEquals(expected, actual);

        // Verify no further interactions with the mocks
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
    }

    @Test
    public void findById_ShouldReturnBook() {
        // Given
        Long id = 1L;
        Book book = new Book();
        BookDto bookDto = new BookDto();
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        BookDto result = bookService.findById(id);

        // Then
        assertEquals(bookDto, result);
        // Verify no further interactions with the mocks
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
    }

    @Test
    public void save_ShouldSaveAndReturnBook() {
        // Given
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        BookDto bookDto = new BookDto();
        Book book = new Book();
        Mockito.when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        BookDto result = bookService.save(createBookRequestDto);

        // Then
        assertEquals(bookDto, result);
    }

    @Test
    public void update_ShouldUpdateAndReturnBook() {
        // Given
        Long bookId = 1L;
        BookDto bookDto = new BookDto();
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        Book book = new Book();

        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);
        Mockito.when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);

        // When
        BookDto result = bookService.update(bookId, createBookRequestDto);

        // Then
        assertEquals(bookDto, result);
    }

    @Test
    public void delete_ShouldDeleteBook() {
        // Given
        Long bookId = 1L;

        Mockito.doNothing().when(bookRepository).deleteById(bookId);

        // When
        bookService.deleteById(bookId);

        // Then
        Mockito.verify(bookRepository).deleteById(bookId);
    }

    @Test
    public void findBooksByCategoryId_ShouldReturnBooksOfACategory() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setName("Science");
        Book book = new Book();
        book.setAuthor("Bob");
        book.setPrice(BigDecimal.valueOf(100));
        book.setCategories(Set.of(category));
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setAuthor("Bob");
        bookDto.setPrice(BigDecimal.valueOf(100));
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(bookRepository.findAllByCategoryId(categoryId, pageable)).thenReturn(List.of(book));
        Mockito.when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDto);

        // When
        List<BookDtoWithoutCategoryIds> actual = bookService.findBooksByCategoryId(categoryId, pageable);

        // Then
        assertEquals(Collections.singletonList(bookDto), actual);

        // Verify no more interactions with the mocks
        Mockito.verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
    }

    @Test
    @DisplayName("Verify findAllByParams() works")
    public void findAllByParams_ShouldReturnAllBooks() {
        // Given
        List<Book> books = getTestBookList();
        List<BookDto> expected = getTestBookDtoList();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        Map<String, String> params = new HashMap<>();
        params.put("authorIn", "pushkin,esenin");
        params.put("titleContains", "poems");
        Specification<Book> sp_1 = Specification.where(null);
        Specification<Book> sp_2 = Specification.where(null);
        Specification<Book> specification = Specification.where(sp_1).and(sp_2);
        when(specificationManager.get("authorIn", new String[]{"pushkin", "esenin"})).thenReturn(sp_1);
        when(specificationManager.get("titleContains", new String[]{"poems"})).thenReturn(sp_2);
        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(bookPage);
        when(bookMapper.toDto(books.get(0))).thenReturn(expected.get(0));
        when(bookMapper.toDto(books.get(1))).thenReturn(expected.get(1));

        // When
        List<BookDto> actual = bookService.findAllByParams(params, pageable);

        // Then
        assertThat(actual).hasSize(2);
        assertEquals(expected, actual);

        // Verify no further interactions with the mocks
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
    }

    private List<Book> getTestBookList() {
        Book book_1 = new Book();
        book_1.setId(1L);
        book_1.setTitle("Test Book 1");

        Book book_2 = new Book();
        book_2.setId(2L);
        book_2.setTitle("Test Book 2");

        return List.of(book_1, book_2);
    }

    private List<BookDto> getTestBookDtoList() {
        BookDto bookDto_1 = new BookDto();
        bookDto_1.setId(1L);
        bookDto_1.setTitle("Test Book 1");

        BookDto bookDto_2 = new BookDto();
        bookDto_2.setId(2L);
        bookDto_2.setTitle("Test Book 2");

        return List.of(bookDto_1, bookDto_2);
    }
}

package com.bookshop.service;

import com.bookshop.dto.BookDto;
import com.bookshop.dto.CreateBookRequestDto;
import com.bookshop.exception.EntityNotFoundException;
import com.bookshop.mapper.BookMapper;
import com.bookshop.model.Book;
import com.bookshop.repository.BookRepository;
import com.bookshop.repository.specification.book.BookSpecificationManager;
import com.bookshop.service.interf.BookService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final BookMapper bookMapper;
    private final BookSpecificationManager specificationManager;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        Book saved = repository.save(bookMapper.toModel(bookDto));
        return bookMapper.toDto(saved);
    }

    @Override
    public List<BookDto> findAll() {
        return repository.findAll().stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto findById(Long id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not found Book by id = " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto bookDto) {
        Book book = bookMapper.toModel(bookDto);
        book.setId(id);
        return bookMapper.toDto(repository.save(book));
    }

    @Override
    public List<BookDto> findAllByParams(Map<String, String> params) {
        Specification<Book> specification = null;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            Specification<Book> sp = specificationManager.get(entry.getKey(),
                    entry.getValue().split(","));
            specification = specification == null
                    ? Specification.where(sp)
                    : specification.and(sp);
        }
        return repository.findAll(specification).stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }
}

package com.bookshop.service.interf;

import com.bookshop.dto.BookDto;
import com.bookshop.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    List<BookDto> findAll();

    BookDto findById(Long id);
}

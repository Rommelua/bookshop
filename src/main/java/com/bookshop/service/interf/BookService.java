package com.bookshop.service.interf;

import com.bookshop.dto.BookDto;
import com.bookshop.dto.BookDtoWithoutCategoryIds;
import com.bookshop.dto.CreateBookRequestDto;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookDto);

    List<BookDto> findAllByParams(Map<String, String> params, Pageable pageable);

    List<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long id, Pageable pageable);
}

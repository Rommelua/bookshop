package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.BookDto;
import com.bookshop.dto.BookDtoWithoutCategoryIds;
import com.bookshop.dto.CreateBookRequestDto;
import com.bookshop.model.Book;
import com.bookshop.model.Category;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
    }

//    @Named("bookFromId")
//    default Book bookFromId(Long id) {
//
//    }
}

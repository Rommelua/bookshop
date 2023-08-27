package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.cart.CartItemDto;
import com.bookshop.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(source = "book.author", target = "bookAuthor")
    CartItemDto toDto(CartItem cartItem);
}

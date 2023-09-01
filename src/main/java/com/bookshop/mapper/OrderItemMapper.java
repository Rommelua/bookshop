package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.order.OrderItemDto;
import com.bookshop.model.OrderItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);

    List<OrderItemDto> toDtoList(List<OrderItem> orderItems);
}

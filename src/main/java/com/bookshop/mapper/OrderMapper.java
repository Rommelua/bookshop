package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.order.OrderDto;
import com.bookshop.model.Order;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "orderItems")
    OrderDto toDto(Order order);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "orderItems", ignore = true)
    Order toModel(OrderDto orderDto);

    List<OrderDto> toDtoList(List<Order> orders);
}

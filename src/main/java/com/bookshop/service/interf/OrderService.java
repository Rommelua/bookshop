package com.bookshop.service.interf;

import com.bookshop.dto.order.OrderDto;
import com.bookshop.dto.order.OrderItemDto;
import com.bookshop.dto.order.PlaceOrderRequestDto;
import com.bookshop.model.Order;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto placeOrder(Long userId, PlaceOrderRequestDto placeOrderRequestDto);

    List<OrderDto> getOrderHistory(Long userId, Pageable pageable);

    OrderDto updateOrderStatus(Long orderId, Order.Status status);

    List<OrderItemDto> getOrderItems(Long userId, Long orderId);
}

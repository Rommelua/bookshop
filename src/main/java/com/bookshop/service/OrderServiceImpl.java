package com.bookshop.service;

import com.bookshop.dto.order.OrderDto;
import com.bookshop.dto.order.OrderItemDto;
import com.bookshop.dto.order.PlaceOrderRequestDto;
import com.bookshop.exception.EntityNotFoundException;
import com.bookshop.mapper.OrderItemMapper;
import com.bookshop.mapper.OrderMapper;
import com.bookshop.model.Book;
import com.bookshop.model.CartItem;
import com.bookshop.model.Order;
import com.bookshop.model.OrderItem;
import com.bookshop.model.ShoppingCart;
import com.bookshop.model.User;
import com.bookshop.repository.CartItemRepository;
import com.bookshop.repository.OrderItemRepository;
import com.bookshop.repository.OrderRepository;
import com.bookshop.repository.ShoppingCartRepository;
import com.bookshop.repository.UserRepository;
import com.bookshop.service.interf.OrderService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public OrderDto placeOrder(Long userId, PlaceOrderRequestDto placeOrderRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find shopping cart by userId " + userId));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.valueOf(Order.Status.PENDING.name()));
        order.setShippingAddress(placeOrderRequestDto.getShippingAddress());
        order.setTotal(shoppingCart.getTotalPrice());
        order = orderRepository.save(order);

        Set<OrderItem> orderItems = getOrderItems(shoppingCart, order);
        orderItemRepository.saveAll(orderItems);
        order.setOrderItems(orderItems);
        cartItemRepository.deleteAll(shoppingCart.getCartItems());
        return orderMapper.toDto(order);
    }

    private Set<OrderItem> getOrderItems(ShoppingCart shoppingCart, Order order) {
        Set<OrderItem> orderItems = new HashSet<>(shoppingCart.getCartItems().size());
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            Book book = cartItem.getBook();

            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setBook(book);
            orderItem.setOrder(order);
            orderItem.setPrice(book.getPrice());

            orderItems.add(orderItem);
        }
        return orderItems;
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + orderId
                        + " not found for user with id " + userId));
        List<OrderItem> orderItems = List.copyOf(order.getOrderItems());
        return orderItemMapper.toDtoList(orderItems);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrderHistory(Long userId, Pageable pageable) {
        List<Order> orders = orderRepository.findAllByUserId(userId, pageable);
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, Order.Status status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        order.setStatus(status);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }
}

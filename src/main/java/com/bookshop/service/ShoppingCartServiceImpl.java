package com.bookshop.service;

import com.bookshop.dto.cart.AddToCartRequestDto;
import com.bookshop.dto.cart.ShoppingCartDto;
import com.bookshop.dto.cart.UpdateCartItemRequestDto;
import com.bookshop.exception.EntityNotFoundException;
import com.bookshop.mapper.ShoppingCartMapper;
import com.bookshop.model.Book;
import com.bookshop.model.CartItem;
import com.bookshop.model.ShoppingCart;
import com.bookshop.model.User;
import com.bookshop.repository.BookRepository;
import com.bookshop.repository.CartItemRepository;
import com.bookshop.repository.ShoppingCartRepository;
import com.bookshop.repository.UserRepository;
import com.bookshop.service.interf.ShoppingCartService;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Transactional(readOnly = true)
    public ShoppingCartDto getById(Long userId) {
        User user = getUserById(userId);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserIdWithCartItems(userId)
                .orElseGet(newShoppingCartSupplier(user));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    public ShoppingCartDto addToCart(AddToCartRequestDto request, Long userId) {
        User user = getUserById(userId);
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(newShoppingCartSupplier(user));

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private Supplier<ShoppingCart> newShoppingCartSupplier(User user) {
        return () -> {
            ShoppingCart newShoppingCart = new ShoppingCart();
            newShoppingCart.setUser(user);
            shoppingCartRepository.save(newShoppingCart);
            return newShoppingCart;
        };
    }

    @Transactional
    public ShoppingCartDto updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequestDto request) {
        CartItem cartItem = getCartItem(userId, cartItemId);
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cartItem.getShoppingCart());
    }

    @Transactional
    public ShoppingCartDto removeCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = getCartItem(userId, cartItemId);
        cartItem.setIsDeleted(true);
        cartItemRepository.saveAndFlush(cartItem);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserIdWithCartItems(userId).get();
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private CartItem getCartItem(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find shopping cart by id " + userId));
        return cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Can't find cart item by id %s for user with id %s", cartItemId, userId))
                );
    }
}

package com.bookshop.service.interf;

import com.bookshop.dto.cart.AddToCartRequestDto;
import com.bookshop.dto.cart.ShoppingCartDto;
import com.bookshop.dto.cart.UpdateCartItemRequestDto;

public interface ShoppingCartService {
    ShoppingCartDto getById(Long id);

    ShoppingCartDto addToCart(AddToCartRequestDto request, Long id);

    ShoppingCartDto updateCartItem(Long id, Long cartItemId, UpdateCartItemRequestDto request);

    ShoppingCartDto removeCartItem(Long id, Long cartItemId);
}

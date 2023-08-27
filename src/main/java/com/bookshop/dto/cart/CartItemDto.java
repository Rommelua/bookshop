package com.bookshop.dto.cart;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private Integer quantity;
}

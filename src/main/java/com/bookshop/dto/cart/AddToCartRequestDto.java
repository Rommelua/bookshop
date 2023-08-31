package com.bookshop.dto.cart;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddToCartRequestDto {
    @NotNull(message = "bookId cannot be null.")
    @Positive
    private Long bookId;
    @NotNull(message = "quantity cannot be null.")
    @Positive
    private int quantity;
}

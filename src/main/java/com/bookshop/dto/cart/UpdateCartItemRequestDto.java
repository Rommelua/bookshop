package com.bookshop.dto.cart;

import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateCartItemRequestDto {
    @Positive
    private int quantity;
}

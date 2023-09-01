package com.bookshop.dto.order;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private Long bookId;
    private int quantity;
    private BigDecimal price;
}

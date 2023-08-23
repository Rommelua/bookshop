package com.bookshop.dto;

import java.math.BigDecimal;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateBookRequestDto {
    @NotNull(message = "Title cannot be null.")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters.")
    private String title;
    @NotNull(message = "Author cannot be null.")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters.")
    private String author;
    @NotNull(message = "ISBN cannot be null.")
    @Size(min = 1, max = 13, message = "ISBN must be between 1 and 13 characters.")
    //@ISBN
    private String isbn;
    @NotNull(message = "Price cannot be null.")
    @Positive
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Long> categoryIds;
}

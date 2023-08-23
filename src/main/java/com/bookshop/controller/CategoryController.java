package com.bookshop.controller;

import com.bookshop.dto.BookDtoWithoutCategoryIds;
import com.bookshop.dto.CategoryDto;
import com.bookshop.dto.CreateCategoryRequestDto;
import com.bookshop.service.interf.BookService;
import com.bookshop.service.interf.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category management", description = "Endpoints for managing Categories")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto create(@RequestBody @Valid CreateCategoryRequestDto dto) {
        return categoryService.save(dto);
    }

    @GetMapping
    @Operation(summary = "Get all Categories", description = "Get a list of all available Categories")
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll();
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Get all Categories", description = "Get a list of all available Categories")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id, Pageable pageable) {
        return bookService.findBooksByCategoryId(id, pageable);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto update(@PathVariable Long id, @RequestBody @Valid CreateCategoryRequestDto dto) {
        return categoryService.update(id, dto);
    }
}

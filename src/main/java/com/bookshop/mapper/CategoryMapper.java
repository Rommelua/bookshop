package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.CategoryDto;
import com.bookshop.dto.CreateCategoryRequestDto;
import com.bookshop.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto categoryDto);
}

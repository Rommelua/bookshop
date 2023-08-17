package com.bookshop.service;

import com.bookshop.dto.CategoryDto;
import com.bookshop.dto.CreateCategoryRequestDto;
import com.bookshop.repository.CategoryRepository;
import com.bookshop.service.interf.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> findAll() {
        return null;
    }

    @Override
    public CategoryDto getById(Long id) {
        return null;
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        return null;
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto categoryDto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}

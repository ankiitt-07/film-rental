package com.filmrental.mapper;

import com.filmrental.model.dto.CategoryDTO;
import com.filmrental.model.entity.Category;

public class CategoryMapper {

    public static CategoryDTO toDto(Category category) {
        if (category == null) return null;

        return new CategoryDTO(
                category.getCategoryId(),
                category.getName(),
                category.getLastUpdate()
        );
    }

    public static Category toEntity(CategoryDTO dto) {
        if (dto == null) return null;

        Category category = new Category();
        category.setCategoryId(dto.category_id());
        category.setName(dto.name());
        category.setLastUpdate(dto.last_update());
        return category;
    }
}
package com.study.core.exceptions.Category;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(UUID id) {
        super(String.format("Категории с id %s не найдено", id));
    }
}

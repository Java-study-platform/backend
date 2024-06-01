package com.study.core.exceptions.Category;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String categoryName) {
        super(String.format("Категория с названием '%s' уже существует", categoryName));
    }
}

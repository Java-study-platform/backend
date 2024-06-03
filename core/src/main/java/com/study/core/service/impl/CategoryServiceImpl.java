package com.study.core.service.impl;

import com.study.core.dto.Category.CreateCategoryModel;
import com.study.core.dto.Category.EditCategoryModel;
import com.study.core.exceptions.Category.CategoryAlreadyExistsException;
import com.study.core.exceptions.Category.CategoryNotFoundException;
import com.study.core.models.Category;
import com.study.core.repository.CategoryRepository;
import com.study.core.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category createCategory(Jwt user, CreateCategoryModel createCategoryModel) {

        if (categoryRepository.existsByName(createCategoryModel.getName())) {
            throw new CategoryAlreadyExistsException(createCategoryModel.getName());
        }

        Category category = new Category();
        category.setName(createCategoryModel.getName());
        category.setDescription(createCategoryModel.getDescription());
        category.setAuthorLogin(user.getClaim("preferred_username")); // TODO: После подключения кейклока заменить на настоящий логин

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category editCategory(EditCategoryModel editCategoryModel, UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (categoryRepository.existsByNameAndIdNot(editCategoryModel.getName(), id)) {
            throw new CategoryAlreadyExistsException(editCategoryModel.getName());
        }

        category.setName(editCategoryModel.getName());
        category.setDescription(editCategoryModel.getDescription());

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        categoryRepository.delete(category);
    }

    @Override
    public Page<Category> getCategories(String queryText, Pageable pageable) {
        if (queryText == null || queryText.isBlank()) {
            return categoryRepository.findAll(pageable);
        }
        else {
            return categoryRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(queryText, queryText, pageable);
        }
    }
}

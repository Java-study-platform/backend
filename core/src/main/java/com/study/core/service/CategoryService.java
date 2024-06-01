package com.study.core.service;


import com.study.core.dto.Category.CreateCategoryModel;
import com.study.core.dto.Category.EditCategoryModel;
import com.study.core.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.UUID;

public interface CategoryService {
    Category createCategory(Principal user, CreateCategoryModel createCategoryModel);

    Category editCategory(EditCategoryModel editCategoryModel, UUID id);

    void deleteCategory(UUID id);

    Page<Category> getCategories(String queryText, Pageable pageable);
}

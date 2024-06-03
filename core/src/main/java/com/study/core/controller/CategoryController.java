package com.study.core.controller;


import com.study.core.dto.Category.CategoryDTO;
import com.study.core.dto.Category.CreateCategoryModel;
import com.study.core.dto.Category.EditCategoryModel;
import com.study.core.dto.DefaultResponse;
import com.study.core.mapper.CategoryMapper;
import com.study.core.models.Category;
import com.study.core.service.CategoryService;
import com.study.core.util.DefaultResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;


@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;


    @PostMapping
    @Operation(summary = "Создать категорию")
    public ResponseEntity<DefaultResponse<CategoryDTO>> createCategory(@AuthenticationPrincipal Jwt user,
                                                                      @Validated @RequestBody CreateCategoryModel createCategoryModel) {

        Category createdCategory = categoryService.createCategory(user, createCategoryModel);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                String.format("Категория '%s' успешно создана", createdCategory.getName()),
                categoryMapper.toDTO(createdCategory)
        ));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Отредактировать категорию")
    public ResponseEntity<DefaultResponse<CategoryDTO>> editCategory(@AuthenticationPrincipal Jwt user,
                                          @Validated @RequestBody EditCategoryModel editCategoryModel,
                                          @PathVariable UUID id) {
        Category editedCategory = categoryService.editCategory(editCategoryModel, id);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                String.format("Категория '%s' успешно изменена", editedCategory.getName()),
                categoryMapper.toDTO(editedCategory)
        ));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить категорию по id")
    public ResponseEntity<DefaultResponse<?>> deleteCategory(@AuthenticationPrincipal Jwt user,
                                            @PathVariable UUID id) {
        categoryService.deleteCategory(id);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Категория успешно удалена",
                null
        ));
    }


    @GetMapping
    @Operation(summary = "Получить список категорий", description = "Список категорий с пагинацией и текстом запроса (поиск одновременно по названию и описанию) ")
    public ResponseEntity<DefaultResponse<Page<CategoryDTO>>> getCategories(@RequestParam(required = false) String queryText,
                                                                           @ParameterObject @PageableDefault(sort="name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Список категорий",
                categoryService.getCategories(queryText, pageable).map(categoryMapper::toDTO)
        ));
    }
}

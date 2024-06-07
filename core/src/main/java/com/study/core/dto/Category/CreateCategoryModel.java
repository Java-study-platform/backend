package com.study.core.dto.Category;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryModel {

    @NotBlank(message = "Название категории не должно быть пустым")
    @NotNull(message = "Название категории не может быть null")
    @Length(min = 1, max = 30, message = "Название категории должно быть длиной от 1 до 30 символов")
    private String name;

    @NotBlank(message = "Описание категории не должно быть пустым")
    @NotNull(message = "Описание категории не может быть null")
    @Length(min = 1, max = 2500, message = "Описание категории должно быть длиной от 1 до 2500 символов")
    private String description;
}

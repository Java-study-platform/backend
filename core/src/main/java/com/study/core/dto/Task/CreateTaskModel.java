package com.study.core.dto.Task;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskModel {

    @NotBlank(message = "Название задачи не должно быть пустым")
    @NotNull(message = "Название задачи не может быть null")
    @Length(min = 1, max = 30, message = "Название задачи должно быть длиной от 1 до 30 символов")
    private String name;

    @NotBlank(message = "Описание задачи не должно быть пустым")
    @NotNull(message = "Описание задачи не может быть null")
    @Length(min = 1, max = 2500, message = "Описание задачи должно быть длиной от 1 до 2500 символов")
    private String description;

    @NotNull(message = "Количество опыта не может быть null")
    @Positive(message = "Количество опыта должно быть строго положительным")
    @Digits(integer = 3, fraction = 0, message = "Количество опыта должно состоять максимум из 3 цифр")
    private long experienceAmount;
}

package com.study.core.dto.Topic;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTopicModel {
    @NotBlank(message = "Название темы не должно быть пустым")
    @NotNull(message = "Название темы не может быть null")
    @Length(min = 1, max = 30, message = "Название темы должно быть длиной от 1 до 30 символов")
    private String name;

    @NotBlank(message = "Материал не может быть пустым")
    @NotNull(message = "Материал не может быть null")
    @Length(min = 1, max = 15000, message = "Материал должен содержать от 1 до 15000 символов")
    private String material;
}

package com.study.core.dto.Test;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTestModel {
    @NotBlank(message = "Входные данные не должны быть пустыми")
    @NotNull(message = "Входные данные не могут быть null")
    private String expectedInput;

    @NotBlank(message = "Выходные данные не должны быть пустыми")
    @NotNull(message = "Выходные данные не могут быть null")
    private String expectedOutput;
}

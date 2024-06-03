package com.study.core.dto.Task;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TaskFilter(
        String content,
        Long experienceAmountMin,
        Long experienceAmountMax,
        UUID topicId,
        UUID categoryId
) {
}

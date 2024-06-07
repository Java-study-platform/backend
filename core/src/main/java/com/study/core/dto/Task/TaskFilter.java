package com.study.core.dto.Task;


import java.util.UUID;

public record TaskFilter(
        String content,
        Long experienceAmountMin,
        Long experienceAmountMax,
        UUID topicId,
        UUID categoryId
) {
}

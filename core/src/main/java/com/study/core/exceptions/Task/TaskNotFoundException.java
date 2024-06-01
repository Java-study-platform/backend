package com.study.core.exceptions.Task;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(UUID id) {
        super(String.format("Задачи с id %s не найдено", id));
    }
}

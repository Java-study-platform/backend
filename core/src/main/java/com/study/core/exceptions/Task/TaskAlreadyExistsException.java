package com.study.core.exceptions.Task;

public class TaskAlreadyExistsException extends RuntimeException {
    public TaskAlreadyExistsException(String taskName) {
        super(String.format("Задача с названием '%s' уже существует", taskName));
    }
}

package com.study.solution.Exceptions.NotFound;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(UUID taskId){
        super(String.format("Задание с id = %s не найдено", taskId));
    }
}

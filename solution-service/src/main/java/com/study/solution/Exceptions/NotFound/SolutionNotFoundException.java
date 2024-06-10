package com.study.solution.Exceptions.NotFound;

import java.util.UUID;

public class SolutionNotFoundException extends RuntimeException{
    public SolutionNotFoundException(UUID solutionId){
        super(String.format("Решение с id = %s не найдено", solutionId));
    }
}

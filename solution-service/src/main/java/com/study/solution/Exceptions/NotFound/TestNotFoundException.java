package com.study.solution.Exceptions.NotFound;

import java.util.UUID;

public class TestNotFoundException extends RuntimeException{
    public TestNotFoundException(UUID testId){
        super(String.format("Тест с id = %s не найдено", testId));
    }
}

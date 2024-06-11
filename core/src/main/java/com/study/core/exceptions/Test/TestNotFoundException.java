package com.study.core.exceptions.Test;

import java.util.UUID;

public class TestNotFoundException extends RuntimeException {
    public TestNotFoundException(UUID id) {
        super(String.format("Тест с id %s не найден", id));
    }
}

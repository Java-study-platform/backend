package com.study.core.exceptions.Chat;

import java.util.UUID;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(UUID id) {
        super(String.format("Чата с id %s не найдено", id));
    }
}

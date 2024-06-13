package com.study.core.exceptions.Message;

import java.util.UUID;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(UUID id) {
        super(String.format("Сообщение с id '%s' не найдено", id));
    }
}

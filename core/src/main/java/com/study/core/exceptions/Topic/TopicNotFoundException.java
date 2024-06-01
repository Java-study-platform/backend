package com.study.core.exceptions.Topic;

import java.util.UUID;

public class TopicNotFoundException extends RuntimeException {
    public TopicNotFoundException(UUID id) {
        super(String.format("Темы с id %s не найдено", id));
    }
}

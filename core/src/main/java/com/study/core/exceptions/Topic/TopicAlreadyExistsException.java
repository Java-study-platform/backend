package com.study.core.exceptions.Topic;

public class TopicAlreadyExistsException extends RuntimeException {
    public TopicAlreadyExistsException(String topicName) {
        super(String.format("Тема с названием '%s' уже существует", topicName));
    }
}

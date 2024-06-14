package com.study.core.exceptions.Message;

import java.util.UUID;

public class ReactionAlreadyExistsException extends RuntimeException {
    public ReactionAlreadyExistsException(UUID id, String reactionName) {
        super(String.format("Пользователь уже реагировал на сообщение '%s' реакцией: '%s'", id, reactionName));
    }
}

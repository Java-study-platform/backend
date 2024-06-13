package com.study.core.exceptions.Message;

import java.util.UUID;

public class ReactionNotFoundException extends RuntimeException {
    public ReactionNotFoundException(UUID id, String reactionName) {
        super(String.format("Пользователь не реагировал на сообщение '%s' реакцией: '%s'", id, reactionName));
    }
}

package com.study.core.exceptions.Message;

import java.util.UUID;

public class MessageAndChatMissmatchException extends RuntimeException {
    public MessageAndChatMissmatchException(UUID messageId, UUID chatId) {
        super(String.format("Сообщение '%s' не принадлежит чату с id '%s'", messageId, chatId));
    }
}

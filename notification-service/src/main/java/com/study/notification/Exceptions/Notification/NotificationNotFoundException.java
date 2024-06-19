package com.study.notification.Exceptions.Notification;

import java.util.UUID;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(UUID notificationId) {
        super(String.format("Уведомление с id = %s не найдено", notificationId));
    }
}

package com.study.notification.Service;


import com.study.notification.DTO.NotificationForUserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public interface NotificationService {
    Integer getAmountOfUnreadNotifications(Jwt user);
    Page<NotificationForUserModel> getNotifications(Pageable pageable, Jwt user, String search);
    void readNotification(Jwt user, UUID notificationId);
    void readAllNotifications(Jwt user);
}

package com.study.notification.Service.Impl;

import com.study.notification.DTO.NotificationForUserModel;
import com.study.notification.Entity.Notification;
import com.study.notification.Exceptions.Notification.NotificationNotFoundException;
import com.study.notification.Mapper.NotificationListMapper;
import com.study.notification.Repository.NotificationRepository;
import com.study.notification.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.study.common.Constants.Consts.EMAIL_CLAIM;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationListMapper notificationListMapper;

    public Integer getAmountOfUnreadNotifications(Jwt user) {
        String email = user.getClaim(EMAIL_CLAIM);

        return notificationRepository.countByIsReadFalse(email);
    }

    public Page<NotificationForUserModel> getNotifications(Pageable pageable, Jwt user, String search) {
        String email = user.getClaim(EMAIL_CLAIM);
        Page<Notification> page = notificationRepository.findAllByUserEmailOrderByIsReadAsc(pageable, email);

        return new PageImpl<>(notificationListMapper.toModelList(page.getContent()), pageable, page.getTotalElements());
    }

    @Transactional
    public void readNotification(Jwt user, UUID notificationId) {
        String email = user.getClaim(EMAIL_CLAIM);

        Notification notification = notificationRepository.findByUserEmailAndId(email, notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));

        notification.setIsRead(true);
        notificationRepository.save(notification);

    }

    @Transactional
    public void readAllNotifications(Jwt user) {
        String email = user.getClaim(EMAIL_CLAIM);

        List<Notification> notifications = notificationRepository.findAllUnreadNotificationsByUserLogin(email);

        notifications = notifications
                .stream()
                .peek((notification) -> notification.setIsRead(true))
                .toList();

        notificationRepository.saveAll(notifications);

    }
}

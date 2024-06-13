package com.study.notification.Kafka;

import com.study.common.DTO.NotificationDTO;
import com.study.notification.Entity.Notification;
import com.study.notification.Mapper.NotificationMapper;
import com.study.notification.Repository.NotificationRepository;
import com.study.notification.Service.Impl.EmailNotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

import static com.study.common.Constants.Consts.NOTIFICATION_GROUP;
import static com.study.common.Constants.Consts.TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final NotificationRepository notificationRepository;
    private final EmailNotificationService sender;
    private final NotificationMapper notificationMapper;

    @KafkaListener(topics = TOPIC, groupId = NOTIFICATION_GROUP, containerFactory = "singleFactory")
    public void listenMessage(NotificationDTO notificationDTO)
            throws MessagingException, UnsupportedEncodingException {

        Notification notification = new Notification();
        notification.setContent(notificationDTO.getContent());
        notification.setTitle(notificationDTO.getTitle());
        notification.setUserEmail(notificationDTO.getUserEmail());

        log.info("Получено сообщение с title: " + notification.getTitle());

        if (notificationDTO.getNeedInHistory()){
            notificationRepository.save(notification);
        }

        sender.sendEmail(notification, notificationDTO);
    }
}

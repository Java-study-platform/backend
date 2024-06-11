package com.study.notification.Service.Impl;


import com.study.common.DTO.NotificationDTO;
import com.study.notification.Entity.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {
    private final JavaMailSender javaMailSender;

    public void sendEmail(Notification notification, NotificationDTO notificationDTO)
            throws UnsupportedEncodingException, MessagingException {
        String senderName = "HITS.CO";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String fromAddress = "gbhfns47@gmail.com";
        helper.setFrom(fromAddress, senderName);
        helper.setTo(notificationDTO.getUserEmail());
        helper.setSubject(notification.getTitle());
        helper.setText(notification.getContent(), true);

        javaMailSender.send(message);
    }
}


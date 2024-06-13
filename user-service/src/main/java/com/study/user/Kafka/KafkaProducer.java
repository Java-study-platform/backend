package com.study.user.Kafka;

import com.study.common.DTO.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.study.common.Constants.Consts.TOPIC;


@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;

    public void sendMessage(
            String email,
            String title,
            String content,
            Boolean needInHistory){


        NotificationDTO notificationDTO = NotificationDTO.builder()
                .content(content)
                .title(title)
                .userEmail(email)
                .needInHistory(needInHistory)
                .createTime(LocalDateTime.now())
                .build();

        kafkaTemplate.send(TOPIC, notificationDTO);
    }
}

package com.study.user.Kafka.consumer;

import com.study.common.DTO.ExperienceDto;
import com.study.user.Service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

import static com.study.common.Constants.Consts.EXPERIENCE_GROUP;
import static com.study.common.Constants.Consts.EXPERIENCE_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final UserServiceImpl userService;

    @KafkaListener(topics = EXPERIENCE_TOPIC, groupId = EXPERIENCE_GROUP, containerFactory = "singleFactory")
    public void listenMessage(ExperienceDto experienceDto)
            throws MessagingException, UnsupportedEncodingException {
        userService.creditExperience(experienceDto);
    }
}

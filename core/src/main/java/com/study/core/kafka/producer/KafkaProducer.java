package com.study.core.kafka.producer;

import com.study.common.DTO.ExperienceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.study.common.Constants.Consts.EXPERIENCE_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, ExperienceDto> kafkaTemplate;

    public void sendMessage(Long experience, String username) {
        ExperienceDto experienceDto = new ExperienceDto(username, experience);
        log.info("Отправляю опыт в размере: " + experience);

        kafkaTemplate.send(EXPERIENCE_TOPIC, experienceDto);
    }
}

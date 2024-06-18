package com.study.solution.Kafka;

import com.study.common.DTO.NotificationDTO;
import com.study.common.DTO.SolutionPassedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.study.common.Constants.Consts.SOLUTION_TOPIC;
import static com.study.common.Constants.Consts.TOPIC;


@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, SolutionPassedDto> kafkaTemplate;

    public void sendMessage(UUID taskId, String username){
         SolutionPassedDto solution = new SolutionPassedDto(taskId, username);

        kafkaTemplate.send(SOLUTION_TOPIC, solution);
    }
}

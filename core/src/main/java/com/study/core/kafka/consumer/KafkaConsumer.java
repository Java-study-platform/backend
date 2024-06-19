package com.study.core.kafka.consumer;

import com.study.common.DTO.SolutionPassedDto;
import com.study.core.service.impl.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

import static com.study.common.Constants.Consts.SOLUTION_GROUP;
import static com.study.common.Constants.Consts.SOLUTION_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final TaskServiceImpl taskService;

    @KafkaListener(topics = SOLUTION_TOPIC, groupId = SOLUTION_GROUP, containerFactory = "singleFactory")
    public void listenMessage(SolutionPassedDto solutionPassedDto)
            throws MessagingException, UnsupportedEncodingException {

        taskService.handleSuccessfulSolution(solutionPassedDto);
    }
}

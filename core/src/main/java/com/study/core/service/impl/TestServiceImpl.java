package com.study.core.service.impl;

import com.study.common.DTO.TestCaseDto;
import com.study.common.Exceptions.ForbiddenException;
import com.study.core.exceptions.Task.TaskNotFoundException;
import com.study.core.mapper.TestCaseListMapper;
import com.study.core.models.Task;
import com.study.core.repository.TaskRepository;
import com.study.core.repository.TestRepository;
import com.study.core.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final TaskRepository taskRepository;
    private final TestCaseListMapper testCaseListMapper;

    @Value("${additional.api-key}")
    private String secretApiKey;

    @Override
    public List<TestCaseDto> getTaskTestCases(String apiKey, UUID taskId) {
        if (apiKey != null && apiKey.equals(secretApiKey)) {
            Task task = taskRepository.findTaskById(taskId)
                    .orElseThrow(() -> new TaskNotFoundException(taskId));

            return testCaseListMapper.toModelList(testRepository.findTestCasesByTask(task));
        }
        else{
            throw new ForbiddenException();
        }
    }
}

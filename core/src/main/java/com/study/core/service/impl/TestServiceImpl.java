package com.study.core.service.impl;

import com.study.common.DTO.TestCaseDto;
import com.study.core.exceptions.Task.TaskNotFoundException;
import com.study.core.mapper.TaskMapper;
import com.study.core.mapper.TestCaseListMapper;
import com.study.core.models.Task;
import com.study.core.models.TestCase;
import com.study.core.repository.TaskRepository;
import com.study.core.repository.TestRepository;
import com.study.core.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final TaskRepository taskRepository;
    private final TestCaseListMapper testCaseListMapper;

    @Override
    public List<TestCaseDto> getTaskTestCases(UUID taskId) {
        Task task = taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        return testCaseListMapper.toModelList(testRepository.findTestCasesByTask(task));
    }
}

package com.study.core.service.impl;

import com.study.common.DTO.TestCaseDto;
import com.study.common.Exceptions.ForbiddenException;
import com.study.core.dto.Test.CreateTestModel;
import com.study.core.dto.Test.EditTestModel;
import com.study.core.exceptions.Task.TaskNotFoundException;
import com.study.core.exceptions.Test.TestNotFoundException;
import com.study.core.mapper.TestCaseListMapper;
import com.study.core.mapper.TestCaseMapper;
import com.study.core.models.Task;
import com.study.core.models.TestCase;
import com.study.core.repository.TaskRepository;
import com.study.core.repository.TestRepository;
import com.study.core.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final TaskRepository taskRepository;
    private final TestCaseMapper testCaseMapper;
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

    @Override
    @Transactional
    public TestCaseDto createTestCase(Jwt user, UUID taskId, CreateTestModel createTestModel) throws TaskNotFoundException {
        Task task = taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        TestCase testCase = new TestCase();
        testCase.setExpectedInput(createTestModel.getExpectedInput());
        testCase.setExpectedOutput(createTestModel.getExpectedOutput());
        testCase.setTask(task);
        testCase.setIndex(testRepository.countAllByTask(task) + 1);

        testRepository.save(testCase);

        return testCaseMapper.toDTO(testCase);
    }

    @Override
    @Transactional
    public void deleteTestCase(Jwt user, UUID testId) throws TestNotFoundException {
        TestCase testCase = testRepository.findById(testId)
                .orElseThrow(() -> new TestNotFoundException(testId));

        List<TestCase> testCaseList = testRepository.findTestCaseByIndexGreaterThan(testCase.getIndex());

        testRepository.delete(testCase);

        for (TestCase test : testCaseList){
            test.setIndex(test.getIndex() - 1);
        }
    }

    @Override
    @Transactional
    public TestCaseDto editTestCase(Jwt user, UUID testId, EditTestModel editTestModel) throws TestNotFoundException {
        TestCase testCase = testRepository.findById(testId)
                        .orElseThrow(() -> new TestNotFoundException(testId));

        testCase.setExpectedInput(editTestModel.getExpectedInput());
        testCase.setExpectedOutput(editTestModel.getExpectedOutput());

        testRepository.save(testCase);

        return testCaseMapper.toDTO(testCase);
    }
}

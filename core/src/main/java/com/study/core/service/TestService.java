package com.study.core.service;

import com.study.common.DTO.TestCaseDto;
import com.study.core.dto.Test.CreateTestModel;
import com.study.core.dto.Test.EditTestModel;
import com.study.core.exceptions.Task.TaskNotFoundException;
import com.study.core.exceptions.Test.TestNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

public interface TestService {
    List<TestCaseDto> getTaskTestCases(String apiKey, UUID taskId);
    TestCaseDto createTestCase(Jwt user, UUID taskId, CreateTestModel createTestModel) throws TaskNotFoundException;
    void deleteTestCase(Jwt user, UUID testId) throws TestNotFoundException;
    TestCaseDto editTestCase(Jwt user, UUID testId, EditTestModel editTestModel) throws TestNotFoundException;
}

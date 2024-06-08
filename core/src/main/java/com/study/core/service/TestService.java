package com.study.core.service;

import com.study.common.DTO.TestCaseDto;

import java.util.List;
import java.util.UUID;

public interface TestService {
    List<TestCaseDto> getTaskTestCases(String apiKey, UUID taskId);
}

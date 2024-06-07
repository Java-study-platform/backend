package com.study.core.service;

import com.study.common.DTO.TestCaseDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

public interface TestService {
    List<TestCaseDto> getTaskTestCases(UUID taskId);
}

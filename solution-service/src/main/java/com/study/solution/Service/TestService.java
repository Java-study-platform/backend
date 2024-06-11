package com.study.solution.Service;

import com.study.solution.DTO.Test.MentorTestDto;
import com.study.solution.DTO.Test.TestDto;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

public interface TestService {
    List<TestDto> getTests(Jwt user, UUID solutionId);
    MentorTestDto getInfoAboutTest(UUID testId);
}

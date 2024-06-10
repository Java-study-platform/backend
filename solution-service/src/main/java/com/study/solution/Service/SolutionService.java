package com.study.solution.Service;

import com.study.solution.DTO.SendTestSolutionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.UUID;

public interface SolutionService {
    String testSolution(Jwt user, UUID taskId, SendTestSolutionRequest code)  throws IOException;
}

package com.study.solution.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.UUID;

public interface SolutionService {
    ResponseEntity<?> testSolution(Jwt user, UUID taskId, String code)  throws IOException;
}

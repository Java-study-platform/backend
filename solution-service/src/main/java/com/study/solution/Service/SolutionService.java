package com.study.solution.Service;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.UUID;

public interface SolutionService {
    ResponseEntity<?> testSolution(UUID taskId, String code, String username)  throws IOException;
}

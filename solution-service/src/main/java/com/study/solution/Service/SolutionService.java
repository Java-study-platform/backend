package com.study.solution.Service;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface SolutionService {
    ResponseEntity<?> testSolution(String code, String username)  throws IOException;
}

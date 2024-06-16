package com.study.solution.Service;

import com.study.solution.DTO.Solution.SendTestSolutionRequest;
import com.study.solution.DTO.Solution.SolutionDto;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface SolutionService {
    CompletableFuture<SolutionDto> testSolution(Jwt user, UUID taskId, SendTestSolutionRequest code)  throws IOException;
    List<SolutionDto> getUserSolutions(Jwt user, UUID taskId);
    SolutionDto getSolution(Jwt user, UUID solutionId);
}

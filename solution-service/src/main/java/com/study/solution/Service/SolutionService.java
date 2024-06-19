package com.study.solution.Service;

import com.study.solution.DTO.Solution.SendTestSolutionRequest;
import com.study.solution.DTO.Solution.SolutionDto;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface SolutionService {
    SolutionDto testSolution(Jwt user, UUID taskId, SendTestSolutionRequest code) throws IOException;

    List<SolutionDto> getSolutions(Jwt user, UUID taskId);

    List<SolutionDto> getUserSolutions(UUID taskId, String username);

    SolutionDto getSolution(Jwt user, UUID solutionId);
}

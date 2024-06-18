package com.study.solution.Service.Impl;

import com.study.solution.Entity.Solution;
import com.study.solution.Exceptions.NotFound.SolutionNotFoundException;
import com.study.solution.Repository.SolutionRepository;
import com.study.solution.Service.AccessControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("acsi")
@RequiredArgsConstructor
public class AccessControlServiceImpl implements AccessControlService {
    private final SolutionRepository solutionRepository;

    @Override
    public boolean canAccessSolution(String userLogin, UUID solutionId) {
        Solution solution = solutionRepository.findSolutionById(solutionId)
                .orElseThrow(() -> new SolutionNotFoundException(solutionId));

        return userLogin.equals(solution.getUsername());
    }
}

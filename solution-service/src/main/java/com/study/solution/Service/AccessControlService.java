package com.study.solution.Service;

import java.util.UUID;

public interface AccessControlService {
    boolean canAccessSolution(String userLogin, UUID solutionId);
}

package com.study.solution.Repository;

import com.study.solution.Entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, UUID> {
    Optional<Solution> findSolutionById(UUID solutionId);
    List<Solution> findAllByUsernameAndTaskId(String username, UUID taskId);
}

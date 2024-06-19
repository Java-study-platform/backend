package com.study.solution.Repository;

import com.study.solution.Entity.Solution;
import com.study.solution.Entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TestRepository extends JpaRepository<Test, UUID> {
    List<Test> findAllBySolution(Solution solution);

    Optional<Test> findTestById(UUID testId);
}

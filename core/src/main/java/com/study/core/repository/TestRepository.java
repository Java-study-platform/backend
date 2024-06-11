package com.study.core.repository;

import com.study.core.models.Task;
import com.study.core.models.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestRepository extends JpaRepository<TestCase, UUID> {
    List<TestCase> findTestCasesByTask(Task task);
    Long countAllByTask(Task task);
    List<TestCase> findTestCaseByIndexGreaterThan(Long index);
}

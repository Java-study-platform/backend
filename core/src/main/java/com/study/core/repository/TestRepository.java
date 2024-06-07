package com.study.core.repository;

import com.study.core.models.Task;
import com.study.core.models.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<TestCase, UUID> {
    List<TestCase> findTestCasesByTask(Task task);
}

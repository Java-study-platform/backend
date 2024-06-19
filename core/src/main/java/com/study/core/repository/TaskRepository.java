package com.study.core.repository;

import com.study.core.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>,
        QuerydslPredicateExecutor<Task> {
    boolean existsByNameAndIdNot(String name, UUID id);

    boolean existsByName(String name);

    Optional<Task> findTaskById(UUID taskId);
}

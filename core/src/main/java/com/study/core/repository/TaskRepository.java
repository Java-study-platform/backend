package com.study.core.repository;

import com.study.core.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>,
                                        QuerydslPredicateExecutor<Task> {
    boolean existsByName(String name);
}

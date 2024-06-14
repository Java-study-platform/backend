package com.study.core.service;


import com.study.core.dto.Task.CreateTaskModel;
import com.study.core.dto.Task.EditTaskModel;
import com.study.core.dto.Task.TaskFilter;
import com.study.core.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public interface TaskService {
    Task createTask(Jwt user, UUID topicId, CreateTaskModel createTaskModel);

    Task editTask(EditTaskModel editTaskModel, UUID id);

    void deleteTask(UUID id);

    Page<Task> getTasks(TaskFilter taskFilter, Pageable pageable);

    Task getTask(UUID id);
}

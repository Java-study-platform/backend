package com.study.core.controller;


import com.study.common.DTO.DefaultResponse;
import com.study.common.util.DefaultResponseBuilder;
import com.study.core.dto.Task.CreateTaskModel;
import com.study.core.dto.Task.EditTaskModel;
import com.study.core.dto.Task.TaskDTO;
import com.study.core.dto.Task.TaskFilter;
import com.study.core.mapper.TaskMapper;
import com.study.core.models.Task;
import com.study.core.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.study.common.Constants.Consts.TASKS;


@RestController
@RequiredArgsConstructor
@Tag(name = "Task")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping(TASKS + "/{topicId}")
    @Operation(summary = "Создать задачу в конкретной теме")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultResponse<TaskDTO>> createTask(@AuthenticationPrincipal Jwt user,
                                                               @Validated @RequestBody CreateTaskModel createTaskModel,
                                                               @PathVariable UUID topicId) {
        Task createdTask = taskService.createTask(user, topicId, createTaskModel);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                String.format("Задача '%s' успешно создана", createdTask.getName()),
                taskMapper.toDTO(createdTask)
        ));
    }

    @PutMapping(TASKS + "/{id}")
    @Operation(summary = "Редактировать задачу")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultResponse<TaskDTO>> editTask(@AuthenticationPrincipal Jwt user,
                                                             @Validated @RequestBody EditTaskModel editTaskModel,
                                                             @PathVariable UUID id) {
        Task editedTask = taskService.editTask(editTaskModel, id);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                String.format("Задача '%s' успешно изменена", editedTask.getName()),
                taskMapper.toDTO(editedTask)
        ));
    }

    @DeleteMapping(TASKS + "/{id}")
    @Operation(summary = "Удалить задачу")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultResponse<?>> deleteTask(@AuthenticationPrincipal Jwt user,
                                                         @PathVariable UUID id) {
        taskService.deleteTask(id);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Задача успешно удалена",
                null
        ));
    }


    @GetMapping(TASKS)
    @Operation(summary = "Получить список задач с фильтром и пагинацией")
    public ResponseEntity<DefaultResponse<Page<TaskDTO>>> getTasks(TaskFilter taskFilter,
                                                                   @ParameterObject @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Список задач",
                taskService.getTasks(taskFilter, pageable).map(taskMapper::toDTO)
        ));
    }

    @GetMapping(TASKS + "/{id}")
    @Operation(summary = "Получить конкретную задачу")
    public ResponseEntity<DefaultResponse<TaskDTO>> getTask(@PathVariable UUID id) {
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Задача",
                taskMapper.toDTO(taskService.getTask(id))
        ));
    }
}

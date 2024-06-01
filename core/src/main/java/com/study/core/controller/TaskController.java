package com.study.core.controller;


import com.study.core.dto.DefaultResponse;
import com.study.core.dto.Task.CreateTaskModel;
import com.study.core.dto.Task.EditTaskModel;
import com.study.core.dto.Task.TaskDTO;
import com.study.core.dto.Task.TaskFilter;
import com.study.core.dto.Topic.CreateTopicModel;
import com.study.core.dto.Topic.EditTopicModel;
import com.study.core.dto.Topic.TopicDTO;
import com.study.core.mapper.TaskMapper;
import com.study.core.models.Task;
import com.study.core.models.Topic;
import com.study.core.service.TaskService;
import com.study.core.util.DefaultResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;



@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping("/{topicId}")
    @Operation(summary = "Создать задачу в конкретной теме")
    public ResponseEntity<DefaultResponse<TaskDTO>> createTask(@AuthenticationPrincipal Principal user,
                                                                @Validated @RequestBody CreateTaskModel createTaskModel,
                                                                @PathVariable UUID topicId) {
        Task createdTask = taskService.createTask(user, topicId, createTaskModel);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                String.format("Задача '%s' успешно создана", createdTask.getName()),
                taskMapper.toDTO(createdTask)
        ));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Редактировать задачу")
    public ResponseEntity<DefaultResponse<TaskDTO>> editTask(@AuthenticationPrincipal Principal user,
                                                               @Validated @RequestBody EditTaskModel editTaskModel,
                                                               @PathVariable UUID id) {
        Task editedTask = taskService.editTask(editTaskModel, id);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                String.format("Задача '%s' успешно изменена", editedTask.getName()),
                taskMapper.toDTO(editedTask)
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу")
    public ResponseEntity<DefaultResponse<?>> deleteTask(@AuthenticationPrincipal Principal user,
                                                          @PathVariable UUID id) {
        taskService.deleteTask(id);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Задача успешно удалена",
                null
        ));
    }


    @GetMapping
    @Operation(summary = "Получить список задач с фильтром и пагинацией")
    public ResponseEntity<DefaultResponse<Page<TaskDTO>>> getTasks(TaskFilter taskFilter,
                                                                   @ParameterObject @PageableDefault(sort="name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Список задач",
                taskService.getTasks(taskFilter, pageable).map(taskMapper::toDTO)
        ));
    }
}

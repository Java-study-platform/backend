package com.study.core.service.impl;

import com.querydsl.core.types.Predicate;
import com.study.core.dto.Task.CreateTaskModel;
import com.study.core.dto.Task.EditTaskModel;
import com.study.core.dto.Task.TaskFilter;
import com.study.core.exceptions.Task.TaskAlreadyExistsException;
import com.study.core.exceptions.Task.TaskNotFoundException;
import com.study.core.exceptions.Topic.TopicNotFoundException;
import com.study.core.models.QTask;
import com.study.core.models.Task;
import com.study.core.models.Topic;
import com.study.core.repository.TaskRepository;
import com.study.core.repository.TopicRepository;
import com.study.core.repository.impl.QPredicates;
import com.study.core.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TopicRepository topicRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task createTask(Jwt user, UUID topicId, CreateTaskModel createTaskModel) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException(topicId));

        if (taskRepository.existsByName(createTaskModel.getName())) {
            throw new TaskAlreadyExistsException(createTaskModel.getName());
        }

        Task task = new Task();
        task.setName(createTaskModel.getName());
        task.setDescription(createTaskModel.getDescription());
        task.setExperienceAmount(createTaskModel.getExperienceAmount());
        task.setAuthorLogin(user.getClaim("preferred_username"));
        task.setTopic(topic);

        return taskRepository.save(task);
    }


    @Override
    @Transactional
    public Task editTask(EditTaskModel editTaskModel, UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if (taskRepository.existsByNameAndIdNot(editTaskModel.getName(), id)) {
            throw new TaskAlreadyExistsException(editTaskModel.getName());
        }

        task.setName(editTaskModel.getName());

        return taskRepository.save(task);
    }


    @Override
    @Transactional
    public void deleteTask(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.delete(task);
    }

    @Override
    public Page<Task> getTasks(TaskFilter taskFilter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(taskFilter.content(), QTask.task.name::containsIgnoreCase)
                .add(taskFilter.content(), QTask.task.description::containsIgnoreCase)
                .add(taskFilter.experienceAmountMin(), QTask.task.experienceAmount::goe)
                .add(taskFilter.experienceAmountMax(), QTask.task.experienceAmount::loe)
                .add(taskFilter.topicId(), QTask.task.topic.id::eq)
                .add(taskFilter.categoryId(), QTask.task.topic.category.id::eq)
                .build();


        return taskRepository.findAll(predicate, pageable);
    }

    @Override
    public Task getTask(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}


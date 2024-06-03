package com.study.core.mapper;


import com.study.core.dto.Task.TaskDTO;
import com.study.core.models.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "topic.id", target = "topicId")
    TaskDTO toDTO(Task task);
}

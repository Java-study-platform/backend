package com.study.core.mapper;

import com.study.core.dto.Topic.TopicDTO;
import com.study.core.models.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses={TaskMapper.class})
public interface TopicMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "topic.chat.id", target = "chatId")
    TopicDTO toDTO(Topic topic);
}

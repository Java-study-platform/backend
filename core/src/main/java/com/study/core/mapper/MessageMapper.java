package com.study.core.mapper;

import com.study.core.dto.Message.MessageDTO;
import com.study.core.enums.ReactionType;
import com.study.core.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "message.parentMessage.id", target = "parentMessageId")
    @Mapping(source = "message.chat.topic.id", target = "topicId")
    @Mapping(source = "currentUserReactions", target = "currentUserReactions")
    MessageDTO toDTO(Message message, Set<ReactionType> currentUserReactions);

    @Mapping(source = "parentMessage.id", target = "parentMessageId")
    @Mapping(source = "message.chat.topic.id", target = "topicId")
    @Mapping(target = "currentUserReactions", ignore = true)
    MessageDTO toDTO(Message message);
}
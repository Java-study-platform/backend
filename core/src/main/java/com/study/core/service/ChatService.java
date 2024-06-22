package com.study.core.service;

import com.study.common.DTO.UserDto;
import com.study.core.dto.Message.MessageDTO;
import com.study.core.dto.Message.ReactMessageModel;
import com.study.core.dto.Message.SendMessageModel;
import com.study.core.dto.Message.UnreactMessageModel;
import com.study.core.models.Message;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    Message sendMessage(SendMessageModel sendMessageModel, UUID chatId, UserDto user);

    List<MessageDTO> getChatHistory(UUID id, UserDto user);

    MessageDTO reactMessage(ReactMessageModel reactMessageModel, UUID id, UserDto user);

    MessageDTO unreactMessage(UnreactMessageModel unreactMessageModel, UUID id, UserDto user);
}

package com.study.core.service;

import com.study.core.dto.Message.MessageDTO;
import com.study.core.dto.Message.ReactMessageModel;
import com.study.core.dto.Message.SendMessageModel;
import com.study.core.dto.Message.UnreactMessageModel;
import com.study.core.models.Message;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    Message sendMessage(SendMessageModel sendMessageModel, UUID chatId, Jwt user);

    List<MessageDTO> getChatHistory(UUID id, Jwt user);

    MessageDTO reactMessage(ReactMessageModel reactMessageModel, UUID id, Jwt user);

    MessageDTO unreactMessage(UnreactMessageModel unreactMessageModel, UUID id, Jwt user);
}

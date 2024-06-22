package com.study.core.controller;


import com.study.common.DTO.UserDto;
import com.study.core.dto.Message.MessageDTO;
import com.study.core.dto.Message.ReactMessageModel;
import com.study.core.dto.Message.SendMessageModel;
import com.study.core.dto.Message.UnreactMessageModel;
import com.study.core.mapper.MessageMapper;
import com.study.core.models.Message;
import com.study.core.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Tag(name = "Чат")
public class WebSocketChatController {
    private final ChatService chatService;
    private final MessageMapper messageMapper;

    @MessageMapping("/chat/{id}")
    @SendTo("/topic/chats/{id}")
    public MessageDTO sendMessage(
            @Validated @Payload SendMessageModel sendMessageModel,
            @DestinationVariable UUID id,
            @AuthenticationPrincipal UserDto userDto) {
        Message sendedMessage = chatService.sendMessage(sendMessageModel, id, userDto);

        return messageMapper.toDTO(sendedMessage);
    }

    @MessageMapping("/chat/{id}/react")
    @SendTo("/topic/chats/{id}")
    public MessageDTO reactMessage(
            @Validated @Payload ReactMessageModel reactMessageModel,
            @DestinationVariable UUID id,
            @AuthenticationPrincipal UserDto userDto) {

        return chatService.reactMessage(reactMessageModel, id, userDto);
    }

    @MessageMapping("/chat/{id}/unreact")
    @SendTo("/topic/chats/{id}")
    public MessageDTO unreactMessage(
            @Validated @Payload UnreactMessageModel unreactMessageModel,
            @DestinationVariable UUID id,
            @AuthenticationPrincipal UserDto userDto) {
        return chatService.unreactMessage(unreactMessageModel, id, userDto);
    }

}

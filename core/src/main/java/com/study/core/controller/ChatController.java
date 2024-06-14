package com.study.core.controller;


import com.study.core.dto.DefaultResponse;
import com.study.core.dto.Message.MessageDTO;
import com.study.core.mapper.MessageMapper;
import com.study.core.service.ChatService;
import com.study.core.util.DefaultResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("/api/learning/chats")
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MessageMapper messageMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Получить историю сообщений чата")
    public ResponseEntity<DefaultResponse<List<MessageDTO>>> getChatHistory(@AuthenticationPrincipal Jwt user, @PathVariable UUID id) {
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "История чата",
                chatService.getChatHistory(id, user)
        ));
    }

}

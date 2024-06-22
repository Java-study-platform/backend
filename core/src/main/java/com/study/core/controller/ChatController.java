package com.study.core.controller;


import com.study.common.DTO.DefaultResponse;
import com.study.common.DTO.UserDto;
import com.study.common.util.DefaultResponseBuilder;
import com.study.core.dto.Message.MessageDTO;
import com.study.core.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/learning/chats")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить историю сообщений чата")
    public ResponseEntity<DefaultResponse<List<MessageDTO>>> getChatHistory(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = (UserDto) authentication.getPrincipal();

        List<MessageDTO> messageDTOS = chatService.getChatHistory(id, userDto);
        log.info(messageDTOS.toString());
        log.info(messageDTOS.get(0).toString());
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "История чата",
                messageDTOS
        ));
    }

}

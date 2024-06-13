package com.study.user.Controllers;

import com.study.common.DTO.UserDto;
import com.study.user.DTO.DefaultResponse;
import com.study.user.Service.UserService;
import com.study.user.util.DefaultResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.study.common.Constants.Consts.GET_USER;
import static com.study.common.Constants.Consts.USERNAME_CLAIM;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Пользователь", description = "Позволяет работать с пользователями")
public class UserController {
    private final UserService userService;

    @GetMapping(GET_USER)
    @Operation(
            summary = "Получение профиля пользователя",
            description = "Позволяет получить информацию о пользователе"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DefaultResponse<UserDto>> getProfile(
            @AuthenticationPrincipal Jwt user
            ) {

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Профиль пользователя получен",
                userService.getUserProfile(user.getClaim(USERNAME_CLAIM))
        ));
    }
}

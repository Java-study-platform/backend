package com.study.user.Controllers;

import com.study.user.DTO.*;
import com.study.user.Service.UserService;
import com.study.user.util.DefaultResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

import static com.study.user.Consts.Consts.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Пользователь", description = "Позволяет работать с пользователями")
public class UserController {
    private final UserService userService;

    @PostMapping(REGISTER_USER)
    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет пользователю зарегистрироваться"
    )
    public ResponseEntity<DefaultResponse<?>> registerUser(
            @RequestBody @Valid UserRegistrationModel userRegistrationModel){
        userService.registerUser(userRegistrationModel);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Пользователь успешной зарегестрирован",
                null
        ));
    }

    @PostMapping(LOGIN_USER)
    @Operation(
            summary = "Авторизация пользователя",
            description = "Позволяет пользователю авторизоваться"
    )
    public ResponseEntity<DefaultResponse<TokenResponse>> loginUser(
            @RequestBody @Valid UserLoginModel userLoginModel
            ) {
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Авторизация прошла успешно",
                userService.loginUser(userLoginModel)
        ));
    }



    @PostMapping(LOGOUT)
    @Operation(
            summary = "Выход из аккаунта",
            description = "Позволяет пользователю выйти из аккаунта"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DefaultResponse<?>> logoutUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        userService.logoutUser(username);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Пользователь успешно вышел из аккаунта",
                null
        ));
    }


    @GetMapping(GET_USER)
    @Operation(
            summary = "Получение профиля пользователя",
            description = "Позволяет получить информацию о пользователе"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DefaultResponse<UserRepresentation>> getProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Профиль пользователя получен",
                userService.getUserProfile(username)
        ));
    }


    @Operation(
            summary = "Назначение ролей пользователю",
            description = "Позволяет назначить роли пользователю (полностью переназначает их)"
    )
    @PutMapping(ASSIGN_ROLES)
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultResponse<?>> assignRoles(@PathVariable UUID id, @Valid @RequestBody AssignUserRoleModel assignUserRoleModel){
        userService.assignRoles(id, assignUserRoleModel);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Роли пользователю успешно назначены",
                null
        ));
    }
}

package com.study.user.Controllers;

import com.study.user.DTO.DefaultResponse;
import com.study.user.DTO.TokenResponse;
import com.study.user.DTO.UserLoginModel;
import com.study.user.DTO.UserRegistrationModel;
import com.study.user.Service.AuthService;
import com.study.user.util.DefaultResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.study.common.Constants.Consts.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Авторизация",
        description = "Контроллер, отвечающий за работу с авторизацией и аутенфикацией пользователей")
public class AuthController {
    private final AuthService authService;

    @PostMapping(REGISTER_USER)
    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет пользователю зарегистрироваться"
    )
    public ResponseEntity<DefaultResponse<?>> registerUser(
            @RequestBody @Valid UserRegistrationModel userRegistrationModel) {
        authService.registerUser(userRegistrationModel);

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
                authService.loginUser(userLoginModel)
        ));
    }

    @PostMapping(LOGOUT)
    @Operation(
            summary = "Выход из аккаунта",
            description = "Позволяет пользователю выйти из аккаунта"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DefaultResponse<?>> logoutUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        authService.logoutUser(username);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Пользователь успешно вышел из аккаунта",
                null
        ));
    }
}

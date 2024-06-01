package com.study.user.Controllers;

import com.study.user.DTO.Response;
import com.study.user.DTO.UserRegistrationModel;
import com.study.user.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.study.user.Consts.Consts.LOGOUT;
import static com.study.user.Consts.Consts.REGISTER_USER;

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
    public ResponseEntity<Response> registerUser(
            @RequestBody @Valid UserRegistrationModel userRegistrationModel){
        return userService.registerUser(userRegistrationModel);
    }

    @PostMapping(LOGOUT)
    @Operation(
            summary = "Выход из аккаунта",
            description = "Позволяет пользователю выйти из аккаунта"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Response> logoutUser(
            @AuthenticationPrincipal String keyCloakUserId
    ){
        return userService.logoutUser(keyCloakUserId);
    }

}

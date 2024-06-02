package com.study.user.Controllers;

import com.study.user.DTO.Response;
import com.study.user.DTO.TokenResponse;
import com.study.user.DTO.UserLoginModel;
import com.study.user.DTO.UserRegistrationModel;
import com.study.user.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.study.user.Consts.Consts.*;

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

    @PostMapping(LOGIN_USER)
    @Operation(
            summary = "Авторизация пользователя",
            description = "Позволяет пользователю авторизоваться"
    )
    public ResponseEntity<TokenResponse> loginUser(
            @RequestBody @Valid UserLoginModel userLoginModel
            ) {
        return ResponseEntity.ok(userService.loginUser(userLoginModel));
    }



    @PostMapping(LOGOUT)
    @Operation(
            summary = "Выход из аккаунта",
            description = "Позволяет пользователю выйти из аккаунта"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Response> logoutUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userService.logoutUser(username);
    }

    @GetMapping(GET_USER)
    @Operation(
            summary = "Получение профиля пользователя",
            description = "Позволяет получить информацию о пользователе"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserRepresentation> getProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userService.getUserProfile(username);
    }
}

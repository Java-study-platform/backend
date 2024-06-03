package com.study.user.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginModel {
    @NotNull(message = "Логин пользователя не должен быть null")
    @NotBlank(message = "Логин пользователя не может быть пустым")
    private String login;

    @NotNull(message = "Пароль пользователя не должен быть null")
    @NotBlank(message = "Пароль пользователя не может быть пустым")
    private String password;
}

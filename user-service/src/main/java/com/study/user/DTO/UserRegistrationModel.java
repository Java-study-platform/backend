package com.study.user.DTO;

import com.study.user.DTO.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationModel {

    @NotNull(message = "Username пользователя не должен быть null")
    @NotBlank(message = "Необходим полный username")
    @Length(min = 1, max = 30, message = "Username должен быть длиной от 1 до 30 символов")
    private String username;

    @NotNull(message = "Email пользователя не должен быть null")
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Необходим валидный email")
    private String email;


    @NotNull(message = "Имя пользователя не должно быть null")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Pattern(regexp = "[a-zA-Zа-яА-Я\\s]+", message = "Имя пользователя должно содержать только латиницу или кириллицу")
    private String firstName;

    @NotNull(message = "Фамилия пользователя не должна быть null")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Pattern(regexp = "[a-zA-Zа-яА-Я\\s]+", message = "Фамилия пользователя должна содержать только латиницу или кириллицу")
    private String lastName;


    @Length(min = 6, message = "Длина пароля должна быть минимум 6 символов")
    @Password
    private String password;
}

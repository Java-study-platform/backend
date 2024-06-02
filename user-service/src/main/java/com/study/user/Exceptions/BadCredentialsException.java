package com.study.user.Exceptions;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("Введены неверные учетные данные пользователя");
    }
}

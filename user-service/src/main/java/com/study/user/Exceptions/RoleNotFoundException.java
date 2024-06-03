package com.study.user.Exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String role) {
        super(String.format("Роль %s не найдена", role));
    }
}

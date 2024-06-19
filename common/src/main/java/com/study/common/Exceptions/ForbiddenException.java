package com.study.common.Exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("У вас нет прав доступа");
    }
}

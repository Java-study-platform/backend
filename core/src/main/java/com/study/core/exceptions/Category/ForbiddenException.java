package com.study.core.exceptions.Category;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(){
        super("У вас нет прав доступа");
    }
}

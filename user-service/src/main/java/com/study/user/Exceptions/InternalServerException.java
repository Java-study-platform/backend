package com.study.user.Exceptions;

public class InternalServerException extends RuntimeException{
    public InternalServerException(String message){
        super(message);
    }
}

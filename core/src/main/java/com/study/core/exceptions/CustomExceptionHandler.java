package com.study.core.exceptions;


import com.study.common.DTO.DefaultResponse;
import com.study.common.Exceptions.ForbiddenException;
import com.study.common.util.DefaultResponseBuilder;
import com.study.core.exceptions.Category.CategoryAlreadyExistsException;
import com.study.core.exceptions.Category.CategoryNotFoundException;
import com.study.core.exceptions.Chat.ChatNotFoundException;
import com.study.core.exceptions.Message.MessageAndChatMissmatchException;
import com.study.core.exceptions.Message.MessageNotFoundException;
import com.study.core.exceptions.Message.ReactionAlreadyExistsException;
import com.study.core.exceptions.Message.ReactionNotFoundException;
import com.study.core.exceptions.Task.TaskAlreadyExistsException;
import com.study.core.exceptions.Task.TaskNotFoundException;
import com.study.core.exceptions.Test.TestNotFoundException;
import com.study.core.exceptions.Topic.TopicAlreadyExistsException;
import com.study.core.exceptions.Topic.TopicNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse<?> handleCategoryAlreadyExists(CategoryAlreadyExistsException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(TopicAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse<?> handleTopicAlreadyExists(TopicAlreadyExistsException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(TaskAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse<?> handleTaskAlreadyExists(TaskAlreadyExistsException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @MessageExceptionHandler(ReactionAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse<?> handleReactionAlreadyExists(ReactionAlreadyExistsException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @MessageExceptionHandler(MessageAndChatMissmatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse<?> handleMessageAndChatMissmatch(MessageAndChatMissmatchException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public DefaultResponse<?> handleCategoryNotFound(CategoryNotFoundException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(TopicNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public DefaultResponse<?> handleTopicNotFound(TopicNotFoundException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(TestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public DefaultResponse<?> handleTestNotFoundException(TestNotFoundException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public DefaultResponse<?> handleTaskNotFound(TaskNotFoundException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @MessageExceptionHandler(ChatNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public DefaultResponse<?> handleChatNotFound(ChatNotFoundException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @MessageExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public DefaultResponse<?> handleMessageNotFound(MessageNotFoundException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @MessageExceptionHandler(ReactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public DefaultResponse<?> handleReactionNotFound(ReactionNotFoundException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse<?> handleIllegalState(IllegalStateException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse<?> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {

        Map<String, List<String>> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));

        return DefaultResponseBuilder.error(
                "Validation failed",
                HttpStatus.BAD_REQUEST,
                errors
        );
    }

    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse<?> handlePropertyReference(PropertyReferenceException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse<?> handleConstraintViolation(jakarta.validation.ConstraintViolationException e) {

        Map<String, List<String>> errors = e.getConstraintViolations().stream()
                .collect(Collectors.groupingBy(
                        violation -> violation.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                ));

        return DefaultResponseBuilder.error(
                "Validation failed",
                HttpStatus.BAD_REQUEST,
                errors
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse<?> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        log.error(exception.getMessage());
        return DefaultResponseBuilder.error(
                "Check your json-format request!",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public DefaultResponse<?> handleForbiddenException(){
        return DefaultResponseBuilder.error(
                "Access Denied",
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public DefaultResponse<?> handleException(Exception e) {
        log.error(e.getMessage(), e.getLocalizedMessage());
        log.error(String.valueOf(e));
        log.error(Arrays.toString(e.getStackTrace()));
        return DefaultResponseBuilder.error(
                "Internal error",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

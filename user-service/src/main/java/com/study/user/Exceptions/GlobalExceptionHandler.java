package com.study.user.Exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.study.common.Exceptions.BadCredentialsException;
import com.study.user.DTO.DefaultResponse;
import com.study.user.util.DefaultResponseBuilder;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse<?> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, List<String>> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.flatMapping(
                                error -> Arrays.stream(Objects.requireNonNull(error.getDefaultMessage()).split(",")),
                                Collectors.toList()
                        )));

        return DefaultResponseBuilder.error(
                "Validation failed",
                HttpStatus.BAD_REQUEST,
                errors
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

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);

        return DefaultResponseBuilder.error(
                e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse<?> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        log.error(e.getMessage(), e);

        return DefaultResponseBuilder.error(
                e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public DefaultResponse<?> handleUserNotFoundException(UserNotFoundException e) {
        log.error(e.getMessage(), e);

        return DefaultResponseBuilder.error(
                e.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public DefaultResponse<?> handleRoleNotFoundException(RoleNotFoundException e) {
        log.error(e.getMessage(), e);

        return DefaultResponseBuilder.error(
                e.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public DefaultResponse<?> handleBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage(), e);

        return DefaultResponseBuilder.error(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(JWTVerificationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public DefaultResponse<?> handleJWTVerificationException(JWTVerificationException e) {
        log.error(e.getMessage(), e);

        return DefaultResponseBuilder.error(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public DefaultResponse<?> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);

        return DefaultResponseBuilder.error(
                e.getMessage(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(responseCode = "500", description = "Что-то пошло не так")
    public DefaultResponse<?> handleException(Exception e){
        log.error(e.getMessage(), e);

        return DefaultResponseBuilder.error(
                "Что-то пошло не так",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

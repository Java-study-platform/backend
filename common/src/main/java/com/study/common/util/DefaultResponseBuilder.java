package com.study.common.util;

import com.study.common.DTO.DefaultResponse;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DefaultResponseBuilder {
    public static <T> DefaultResponse<T> success(String message, T data) {
        return DefaultResponse.<T>builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .timestamp(new Date())
                .data(data)
                .build();
    }

    public static <T> DefaultResponse<T> error(String message, HttpStatus status, Map<String, List<String>> errors) {
        return DefaultResponse.<T>builder()
               .status(status)
               .statusCode(status.value())
               .message(message)
               .timestamp(new Date())
               .data(null)
               .errors(errors)
               .build();
    }

    public static <T> DefaultResponse<T> error(String message, HttpStatus status) {
        return error(message, status, null);
    }
}

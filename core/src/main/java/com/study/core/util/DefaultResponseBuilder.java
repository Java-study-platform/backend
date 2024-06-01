package com.study.core.util;

import com.study.core.dto.DefaultResponse;
import org.springframework.http.HttpStatus;

import java.util.Date;

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

    public static <T> DefaultResponse<T> error(String message, HttpStatus status) {
        return DefaultResponse.<T>builder()
               .status(status)
               .statusCode(status.value())
               .message(message)
               .timestamp(new Date())
               .data(null)
               .build();
    }
}

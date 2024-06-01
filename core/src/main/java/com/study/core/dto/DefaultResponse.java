package com.study.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultResponse<T> {
    private HttpStatus status;
    private int statusCode;
    private String message;
    private Date timestamp;
    private T data;
}

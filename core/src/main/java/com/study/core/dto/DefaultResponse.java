package com.study.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private Map<String, List<String>> errors;
}

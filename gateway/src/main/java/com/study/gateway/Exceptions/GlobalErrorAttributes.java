package com.study.gateway.Exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {

        Map<String, Object> errorResponse = super.getErrorAttributes(request, options);
        Throwable error = super.getError(request);
        if (error instanceof org.springframework.security.access.AccessDeniedException) {
            log.info(Arrays.toString(error.getStackTrace()));
            errorResponse.put("status", HttpStatus.FORBIDDEN.value());
            errorResponse.put("message", "Access denied, maybe you have problem with your token");
            errorResponse.remove("path");
            errorResponse.remove("error");
            errorResponse.remove("requestId");
        }

        return errorResponse;
    }
}

package com.study.core.controller;


import com.study.common.DTO.TestCaseDto;
import com.study.core.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.study.common.Constants.Consts.GET_TEST_CASES;

@RestController
@RequiredArgsConstructor
@Tag(name = "Тест-кейсы", description = "Контроллер, отвечающий за работу с тест-кейсами")
public class TestController {
    private final TestService testService;

    @GetMapping(GET_TEST_CASES + "/{taskId}")
    @Operation(
            summary = "Получение тестов для задачи",
            description = "Позволяет получить тесты, относящиеся к конкретной задаче"
    )
    public List<TestCaseDto> getTaskTestCases(
            HttpServletRequest request,
            @AuthenticationPrincipal Jwt user,
            @PathVariable UUID taskId){
        String apiKey = request.getHeader("X-API-KEY");

        return testService.getTaskTestCases(apiKey, taskId);
    }
}

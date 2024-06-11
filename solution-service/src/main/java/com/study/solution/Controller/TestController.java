package com.study.solution.Controller;

import com.study.common.DTO.DefaultResponse;
import com.study.common.util.DefaultResponseBuilder;
import com.study.solution.DTO.Test.MentorTestDto;
import com.study.solution.DTO.Test.TestDto;
import com.study.solution.Service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.study.common.Constants.Consts.GET_TESTS;
import static com.study.common.Constants.Consts.GET_TEST_INFO;

@RestController
@RequiredArgsConstructor
@Tag(name = "Тесты", description = "Отвечает за работу с тестами решений")
public class TestController {
    private final TestService testService;

    @GetMapping(GET_TESTS)
    @Operation(
            summary = "Получение тестов решения",
            description = "Позволяет пользователю получить вердикты по тестам для конкретного решения"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DefaultResponse<List<TestDto>>> getTests(
            @AuthenticationPrincipal Jwt user,
            @RequestParam(name = "solutionId") UUID solutionId
            ){
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Информация о тестах успешно получена",
                testService.getTests(user, solutionId)
        ));
    }

    @GetMapping(GET_TEST_INFO)
    @Operation(
            summary = "Получение подробной информации о тесте",
            description = "Позволяет ментору получить подробную информацию о тесте"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DefaultResponse<MentorTestDto>> getInfoAboutTest(
            @AuthenticationPrincipal Jwt user,
            @RequestParam(name = "testId") UUID testId
    ){
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Информация о тесте успешно получена",
                testService.getInfoAboutTest(testId)
        ));
    }
}

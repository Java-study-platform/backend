package com.study.core.controller;


import com.study.common.DTO.DefaultResponse;
import com.study.common.DTO.TestCaseDto;
import com.study.common.util.DefaultResponseBuilder;
import com.study.core.dto.Test.CreateTestModel;
import com.study.core.dto.Test.EditTestModel;
import com.study.core.service.TestService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.study.common.Constants.Consts.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Тест-кейсы", description = "Контроллер, отвечающий за работу с тест-кейсами")
public class TestController {
    private final TestService testService;

    @GetMapping(GET_TESTS_FOR_SERVICE + "/{taskId}")
    @Operation(
            summary = "Получение тестов для задачи",
            description = "Позволяет получить тесты, относящиеся к конкретной задаче"
    )
    @Hidden
    public List<TestCaseDto> getTaskTestCasesForService(
            HttpServletRequest request,
            @AuthenticationPrincipal Jwt user,
            @PathVariable UUID taskId) {
        String apiKey = request.getHeader("X-API-KEY");

        return testService.getTaskTestCases(apiKey, taskId);
    }

    @GetMapping(TEST_CASES + "/{taskId}")
    @Operation(
            summary = "Получение тестов для задачи",
            description = "Позволяет администратору получить тесты, относящиеся к конкретной задаче"
    )
    @SecurityRequirement(name = "bearerAuth")
    public List<TestCaseDto> getTaskTestCases(
            @AuthenticationPrincipal Jwt user,
            @PathVariable UUID taskId) {
        return testService.getTaskTestCasesForAdmin(taskId);
    }

    @PostMapping(TEST_CASES + "/{taskId}")
    @Operation(
            summary = "Создание тест-кейса",
            description = "Позволяет администратору создать тест-кейс для конкретного задания"
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultResponse<TestCaseDto>> createTestCase(
            @AuthenticationPrincipal Jwt user,
            @PathVariable UUID taskId,
            @Validated @RequestBody CreateTestModel createTestModel
    ) {
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Тест кейс успешно добавлен",
                testService.createTestCase(user, taskId, createTestModel)
        ));
    }

    @DeleteMapping(CHANGE_TEST_CASE)
    @Operation(
            summary = "Удаление тест-кейса",
            description = "Позволяет администратору удалить тест-кейс"
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultResponse<?>> deleteTestCase(
            @AuthenticationPrincipal Jwt user,
            @RequestParam(name = "testId") UUID testId

    ) {
        testService.deleteTestCase(user, testId);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Тест кейс успешно удален",
                null
        ));
    }

    @PutMapping(CHANGE_TEST_CASE)
    @Operation(
            summary = "Изменение тест-кейса",
            description = "Позволяет администратору изменить тест-кейс"
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultResponse<TestCaseDto>> editTestCase(
            @AuthenticationPrincipal Jwt user,
            @RequestParam(name = "testId") UUID testId,
            @RequestBody EditTestModel editTestModel
    ) {
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Тест кейс успешно изменен",
                testService.editTestCase(user, testId, editTestModel)
        ));
    }
}

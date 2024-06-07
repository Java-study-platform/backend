package com.study.core.controller;


import com.study.common.DTO.TestCaseDto;
import com.study.core.service.TestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.study.common.Constants.Consts.GET_TESTS;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Тест-кейсы", description = "Контроллер, отвечающий за работу с тест-кейсами")
public class TestController {
    private final TestService testService;

    @GetMapping(GET_TESTS + "{taskId}")
    public List<TestCaseDto> getTaskTestCases(@AuthenticationPrincipal Jwt user,
                                              @PathVariable UUID taskId){
        return testService.getTaskTestCases(taskId);
    }
}

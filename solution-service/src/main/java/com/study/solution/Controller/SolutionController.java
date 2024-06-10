package com.study.solution.Controller;

import com.study.common.DTO.DefaultResponse;
import com.study.common.util.DefaultResponseBuilder;
import com.study.solution.DTO.SendTestSolutionRequest;
import com.study.solution.Service.SolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.UUID;

import static com.study.common.Constants.Consts.SOLUTIONS;

@Controller
@RequiredArgsConstructor
@Tag(name = "Решения", description = "Отвечает за обработку решений")
public class SolutionController {
    private final SolutionService solutionService;

    @PostMapping(SOLUTIONS)
    @Operation(
            summary = "Отправка решения",
            description = "Позволяет пользователю отправить решение на проверку"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DefaultResponse<String>> sendSolution(
            @AuthenticationPrincipal Jwt user,
            @RequestParam(name = "taskId") UUID taskId,
            @RequestBody SendTestSolutionRequest code) throws IOException {
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                solutionService.testSolution(user, taskId, code),
                null));
    }
}

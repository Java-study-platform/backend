package com.study.solution.Controller;

import com.study.common.DTO.DefaultResponse;
import com.study.common.util.DefaultResponseBuilder;
import com.study.solution.DTO.Solution.SendTestSolutionRequest;
import com.study.solution.DTO.Solution.SolutionDto;
import com.study.solution.Service.SolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
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
    public ResponseEntity<DefaultResponse<SolutionDto>> sendSolution(
            @AuthenticationPrincipal Jwt user,
            @RequestParam(name = "taskId") UUID taskId,
            @RequestBody SendTestSolutionRequest code) throws IOException {
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Решение успешно отправлено",
                solutionService.testSolution(user, taskId, code)));
    }

    @GetMapping(SOLUTIONS + "/{taskId}")
    @Operation(
            summary = "Получение решений конкретной задачи",
            description = "Позволяет получить список решения, отправленных на указанную задачу"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DefaultResponse<List<SolutionDto>>> getSolutions(
            @AuthenticationPrincipal Jwt user,
            @PathVariable(name = "taskId") UUID taskId
    ){
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Решения успешно получены",
                solutionService.getUserSolutions(user, taskId)
        ));
    }

    @GetMapping(SOLUTIONS)
    @Operation(
            summary = "Получение решения",
            description = "Позволяет пользователю получить конкретное решение по UUID"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DefaultResponse<SolutionDto>> getSolution(
            @AuthenticationPrincipal Jwt user,
            @RequestParam(name = "solutionId") UUID solutionId
    ) {
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Решение успешно получено",
                solutionService.getSolution(user, solutionId)
        ));
    }
}

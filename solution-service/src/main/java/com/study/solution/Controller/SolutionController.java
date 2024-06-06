package com.study.solution.Controller;

import com.study.solution.Service.SolutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Tag(name = "Решения", description = "Отвечает за обработку решений")
public class SolutionController {
    private final SolutionService solutionService;

    @PostMapping("/test")
    @Operation(
            summary = "Выход из аккаунта",
            description = "Позволяет пользователю выйти из аккаунта"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> test() throws IOException {
        return solutionService.testSolution("", "");
    }
}

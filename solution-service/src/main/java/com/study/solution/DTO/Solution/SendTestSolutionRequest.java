package com.study.solution.DTO.Solution;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendTestSolutionRequest {
    @NotNull
    private String code;
}

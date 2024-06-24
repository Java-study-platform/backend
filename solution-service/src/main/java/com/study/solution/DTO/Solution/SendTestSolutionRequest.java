package com.study.solution.DTO.Solution;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SendTestSolutionRequest {
    @NotNull
    @NotBlank
    @Size(min = 1)
    private String code;
}

package com.study.solution.DTO.Solution;

import com.study.common.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolutionDto {
    private UUID id;
    private String solutionCode;
    private LocalDateTime createTime;
    private String username;
    private UUID taskId;
    private Long testIndex;
    private Status status;
}

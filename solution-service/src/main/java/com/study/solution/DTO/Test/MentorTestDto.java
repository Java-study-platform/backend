package com.study.solution.DTO.Test;

import com.study.common.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorTestDto {
    private UUID id;
    private Long testIndex;
    private LocalDateTime testTime;
    private String testInput;
    private String testOutput;
    private Status status;
}

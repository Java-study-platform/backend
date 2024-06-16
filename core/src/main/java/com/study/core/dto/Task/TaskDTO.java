package com.study.core.dto.Task;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private UUID id;
    private String name;
    private String description;
    private long experienceAmount;
    private long timeLimit;
    private UUID topicId;
    private String authorLogin;
    private LocalDateTime createTime;
    private LocalDateTime modifiedDate;
}

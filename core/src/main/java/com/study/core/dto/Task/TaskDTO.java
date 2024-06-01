package com.study.core.dto.Task;


import com.study.core.models.Topic;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private UUID topicId;
    private String authorLogin;
    private LocalDateTime createTime;
    private LocalDateTime modifiedDate;
}

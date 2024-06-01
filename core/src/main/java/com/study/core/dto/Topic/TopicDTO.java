package com.study.core.dto.Topic;

import com.study.core.dto.Task.TaskDTO;
import com.study.core.models.Category;
import com.study.core.models.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicDTO {
    private UUID id;
    private String name;
    private UUID categoryId;
    private List<TaskDTO> tasks;
    private String authorLogin;
    private LocalDateTime createTime;
    private LocalDateTime modifiedDate;
}

package com.study.core.dto.Category;

import com.study.core.dto.Topic.TopicDTO;
import com.study.core.models.Topic;
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
public class CategoryDTO {
    private UUID id;
    private String name;
    private String description;
    private String authorLogin;
    private List<TopicDTO> topics;
    private LocalDateTime createTime;
    private LocalDateTime modifiedDate;
}

package com.study.core.dto.Message;

import com.study.core.models.Message;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageModel {
    @NotBlank(message = "Нельзя отправить пустое сообщение")
    @NotNull(message = "Текст сообщения не может быть равен null")
    private String content;

    private UUID parentMessageId;
}

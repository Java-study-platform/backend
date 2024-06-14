package com.study.core.dto.Message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageModel {
    @NotBlank(message = "Нельзя отправить пустое сообщение")
    @NotNull(message = "Текст сообщения не может быть равен null")
    @Length(message = "Текст сообщения должен содержать от 1 до 6000 символов")
    private String content;

    private UUID parentMessageId;
}

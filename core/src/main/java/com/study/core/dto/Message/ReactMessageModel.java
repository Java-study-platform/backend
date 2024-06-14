package com.study.core.dto.Message;

import com.study.core.enums.ReactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactMessageModel {
    @NotNull(message = "Сообщение id не может быть null")
    private UUID messageId;

    @NotNull(message = "Тип реакции не может быть null")
    private ReactionType reactionType;
}

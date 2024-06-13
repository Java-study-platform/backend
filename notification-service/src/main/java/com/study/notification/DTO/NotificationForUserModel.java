package com.study.notification.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель для уведомлений, которые получает пользователь")
public class NotificationForUserModel {
    @Schema(description = "Идентификатор уведомления")
    private UUID id;

    @Schema(description = "Заголовок")
    private String title;

    @Schema(description = "Содержание уведомления")
    private String content;

    @Schema(description = "Время создания")
    private LocalDateTime createTime;

    @Schema(description = "Статус прочтения")
    private Boolean isRead;
}

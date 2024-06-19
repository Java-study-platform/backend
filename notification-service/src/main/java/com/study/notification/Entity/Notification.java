package com.study.notification.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность уведомлений")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Идентификатор уведомления")
    private UUID id;

    @Column(name = "title")
    @Schema(description = "Заголовок")
    private String title;

    @Column(name = "content")
    @Schema(description = "Содержание уведомления")
    private String content;

    @Column(name = "user_email")
    @Schema(description = "Email получателя")
    private String userEmail;

    @Column(name = "create_time")
    @Schema(description = "Время создания")
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "is_read")
    @Schema(description = "Статус прочтения")
    private Boolean isRead = false;
}

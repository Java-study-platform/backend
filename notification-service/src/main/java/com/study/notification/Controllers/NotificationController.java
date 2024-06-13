package com.study.notification.Controllers;

import com.study.common.DTO.DefaultResponse;
import com.study.common.util.DefaultResponseBuilder;
import com.study.notification.DTO.NotificationForUserModel;
import com.study.notification.Service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.study.common.Constants.Consts.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Уведомления", description = "Контроллер, отвечающий за работу с уведомлениями")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping(GET_NOTIFICATIONS)
    @Operation(
            summary = "Получение уведомлений",
            description = "Позволяет получить список отсортированных уведомлений с пагинацией"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DefaultResponse<Page<NotificationForUserModel>>> getNotifications(
            @AuthenticationPrincipal Jwt user,
            @ParameterObject @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "search", required = false) String search
            ){
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Уведомления успешно получены",
                notificationService.getNotifications(pageable, user, search)
        ));
    }

    @GetMapping(GET_UNREAD_NOTIFICATIONS)
    @Operation(
            summary = "Получение количества непрочитанных уведомлений",
            description = "Позволяет получить количество уведомлений, которые пользователь не прочитал"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DefaultResponse<Integer>> getAmountOfUnreadNotifications(@AuthenticationPrincipal Jwt user){
        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Количество непрочитанных уведомлений успешно получено",
                notificationService.getAmountOfUnreadNotifications(user)
        ));
    }

    @PostMapping(READ_NOTIFICATION)
    @Operation(
            summary = "Прочтение уведомления",
            description = "Позволяет прочитать уведомление, чтобы оно было помечено, как прочитанное"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DefaultResponse<?>> readNotification(
            @AuthenticationPrincipal Jwt user,
            @RequestParam(name = "notificationId") UUID notificationId){
        notificationService.readNotification(user, notificationId);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Уведомление успешно прочитано",
                null
        ));
    }

    @PostMapping(READ_ALL_NOTIFICATIONS)
    @Operation(
            summary = "Прочтение всех уведомлений",
            description = "Позволяет пометить все уведомления, как прочитанные"
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<DefaultResponse<?>> readAllNotification(
            @AuthenticationPrincipal Jwt user){

        notificationService.readAllNotifications(user);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Уведомления успешно прочитаны",
                null
        ));
    }
}

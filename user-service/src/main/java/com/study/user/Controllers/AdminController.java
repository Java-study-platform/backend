package com.study.user.Controllers;

import com.study.user.DTO.AssignUserRoleModel;
import com.study.user.DTO.DefaultResponse;
import com.study.user.Service.AdminService;
import com.study.user.util.DefaultResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.study.common.Constants.Consts.ASSIGN_ROLES;


@RestController
@RequiredArgsConstructor
@Tag(name = "Администратор", description = "Предоставляет функции, доступные онли админу")
public class AdminController {
    private final AdminService adminService;

    @Operation(
            summary = "Назначение ролей пользователю",
            description = "Позволяет назначить роли пользователю (полностью переназначает их)"
    )
    @PutMapping(ASSIGN_ROLES)
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DefaultResponse<?>> assignRoles(@PathVariable UUID userId,
                                                          @Valid @RequestBody AssignUserRoleModel assignUserRoleModel) {
        adminService.assignRoles(userId, assignUserRoleModel);

        return ResponseEntity.ok(DefaultResponseBuilder.success(
                "Роли пользователю успешно назначены",
                null
        ));
    }
}

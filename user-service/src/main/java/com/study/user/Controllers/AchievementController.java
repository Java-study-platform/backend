package com.study.user.Controllers;

import com.study.user.DTO.AchievementDto;
import com.study.user.Service.AchievementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.study.common.Constants.Consts.GET_ACHIEVEMENTS;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Достижения", description = "Контроллер, отвечающий за достижения")
public class AchievementController {
    private final AchievementService achievementService;

    @Operation(
            summary = "Получение списка достижений",
            description = "Позволяет получить полный список достижений"
    )
    @GetMapping(GET_ACHIEVEMENTS)
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<AchievementDto>> getAllUserAchievements(@AuthenticationPrincipal Jwt user){
        return ResponseEntity.ok(achievementService.getAllAchievements(user));
    }
}

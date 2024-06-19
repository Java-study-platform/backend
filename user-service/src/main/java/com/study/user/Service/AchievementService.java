package com.study.user.Service;

import com.study.user.DTO.AchievementDto;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface AchievementService {
    List<AchievementDto> getAllAchievements(Jwt user);
}

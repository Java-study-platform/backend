package com.study.user.Service.Impl;

import com.study.user.DTO.AchievementDto;
import com.study.user.Entity.Achievement;
import com.study.user.Entity.AchievementProgress;
import com.study.user.Entity.User;
import com.study.user.Exceptions.UserNotFoundException;
import com.study.user.Mapper.AchievementMapper;
import com.study.user.Repository.AchievementProgressRepository;
import com.study.user.Repository.AchievementRepository;
import com.study.user.Repository.UserRepository;
import com.study.user.Service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.study.common.Constants.Consts.USERNAME_CLAIM;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementMapper achievementMapper;

    @Override
    public List<AchievementDto> getAllAchievements(Jwt userJwt) {
        User user = userRepository.findByUsername(userJwt.getClaim(USERNAME_CLAIM))
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        List<Achievement> achievements = achievementRepository.findAll();
        List<AchievementDto> dtos = new ArrayList<>();

        for (Achievement achievement : achievements){
            AchievementProgress achievementProgress = achievementProgressRepository.findByUserAndAchievement(user, achievement);

            dtos.add(achievementMapper.toDto(achievement, achievementProgress.getProgress()));
        }

        return dtos;
    }
}

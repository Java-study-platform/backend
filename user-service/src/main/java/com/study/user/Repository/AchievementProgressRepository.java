package com.study.user.Repository;

import com.study.user.Entity.Achievement;
import com.study.user.Entity.AchievementProgress;
import com.study.user.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AchievementProgressRepository extends JpaRepository<AchievementProgress, UUID> {
    List<AchievementProgress> findAllByUserAndIsObtainedIsFalse(User user);

    AchievementProgress findByUserAndAchievement(User user, Achievement achievement);
}

package com.study.user.Repository;

import com.study.user.Entity.AchievementProgress;
import com.study.user.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface AchievementProgressRepository extends JpaRepository<AchievementProgress, UUID> {
    List<AchievementProgress> findAllByUserAndIsObtainedIsFalse(User user);
}

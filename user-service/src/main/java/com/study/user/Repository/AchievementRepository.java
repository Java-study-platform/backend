package com.study.user.Repository;

import com.study.user.Entity.Achievement;
import com.study.user.Entity.AchievementProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AchievementRepository extends JpaRepository<Achievement, UUID> {
}

package com.study.user.Mapper;

import com.study.user.DTO.AchievementDto;
import com.study.user.Entity.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AchievementMapper {
    @Mapping(target = "progress", source = "progress")
    AchievementDto toDto(Achievement achievement, Long progress);
}

package com.study.user.Mapper;

import com.study.user.DTO.AchievementDto;
import com.study.user.Entity.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AchievementMapper {
    @Mappings(value = {
            @Mapping(target = "progress", source = "progress"),
            @Mapping(target = "isObtained", source = "isObtained")
    })
    AchievementDto toDto(Achievement achievement, Long progress, Boolean isObtained);
}

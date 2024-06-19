package com.study.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementDto {
    private UUID id;
    private String name;
    private String description;
    private Long amountToObtain;
    private Long progress;
    private Boolean isObtained;
}

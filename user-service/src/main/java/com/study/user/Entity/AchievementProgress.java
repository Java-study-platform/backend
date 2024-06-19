package com.study.user.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "achievement_progresses")
@AllArgsConstructor
@NoArgsConstructor
public class AchievementProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "achievement_id", referencedColumnName = "id")
    private Achievement achievement;

    @NotNull
    private Long progress = 0L;

    @NotNull
    @Column(name = "is_obtained", nullable = false)
    private Boolean isObtained = false;
}

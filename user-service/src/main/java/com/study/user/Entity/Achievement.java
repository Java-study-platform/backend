package com.study.user.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Достижение
 */
@Setter
@Getter
@Entity
@Table(name = "achievements")
@AllArgsConstructor
@NoArgsConstructor
public class Achievement {
    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Название достижения
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Описание достижения
     */
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;
}

package com.study.core.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "test_cases")
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private Long index;

    @NotNull
    @Column(nullable = false)
    private String expectedInput;

    @NotNull
    @Column(nullable = false)
    private String expectedOutput;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}

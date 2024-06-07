package com.study.solution.Entity;

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
    private Long index;

    @NotNull
    private String expectedInput;

    @NotNull
    private String expectedOutput;
}

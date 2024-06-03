package com.study.solution.Entity;

import com.study.solution.Enum.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "solutions")
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String solutionCode;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID taskId;

    @NotNull
    private Status status;
}

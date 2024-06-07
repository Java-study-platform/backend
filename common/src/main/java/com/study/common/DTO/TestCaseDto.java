package com.study.common.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseDto {
    private UUID id;
    private Long index;
    private String expectedInput;
    private String expectedOutput;
}

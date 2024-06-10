package com.study.solution.Mapper;

import com.study.solution.DTO.Solution.SolutionDto;
import com.study.solution.Entity.Solution;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolutionMapper {
    SolutionDto toDTO(Solution solution);
}

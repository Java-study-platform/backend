package com.study.solution.Mapper;

import com.study.solution.DTO.Solution.SolutionDto;
import com.study.solution.Entity.Solution;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SolutionMapper.class})
public interface SolutionListMapper {
    List<SolutionDto> toModelList(List<Solution> solutions);
}

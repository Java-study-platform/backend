package com.study.solution.Mapper;

import com.study.solution.DTO.Test.TestDto;
import com.study.solution.Entity.Test;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TestMapper.class})
public interface TestListMapper {
    List<TestDto> toModelList(List<Test> tests);
}

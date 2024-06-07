package com.study.core.mapper;

import com.study.common.DTO.TestCaseDto;
import com.study.core.models.TestCase;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TestMapper.class)
public interface TestCaseListMapper {
    List<TestCaseDto> toModelList(List<TestCase> testCases);
}

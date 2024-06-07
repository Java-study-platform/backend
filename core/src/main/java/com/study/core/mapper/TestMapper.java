package com.study.core.mapper;

import com.study.common.DTO.TestCaseDto;
import com.study.core.models.TestCase;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestMapper {
    TestCaseDto toDTO(TestCase testCase);
}

package com.study.core.mapper;

import com.study.common.DTO.TestCaseDto;
import com.study.core.models.TestCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TestMapper {
    @Mapping(source = "task.timeLimit", target = "timeLimit")
    TestCaseDto toDTO(TestCase testCase);

    default long mapTimeLimit(Long timeLimit) {
        return timeLimit != null ? timeLimit : 0L;
    }
}

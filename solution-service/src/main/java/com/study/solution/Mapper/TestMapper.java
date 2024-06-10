package com.study.solution.Mapper;

import com.study.solution.DTO.Test.MentorTestDto;
import com.study.solution.DTO.Test.TestDto;
import com.study.solution.Entity.Test;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestMapper {
    TestDto toDTO(Test test);
    MentorTestDto toMentorDTO(Test test);
}

package com.study.solution.Service.Impl;

import com.study.common.Exceptions.ForbiddenException;
import com.study.solution.DTO.Test.MentorTestDto;
import com.study.solution.DTO.Test.TestDto;
import com.study.solution.Entity.Solution;
import com.study.solution.Exceptions.NotFound.SolutionNotFoundException;
import com.study.solution.Exceptions.NotFound.TestNotFoundException;
import com.study.solution.Mapper.TestListMapper;
import com.study.solution.Mapper.TestMapper;
import com.study.solution.Repository.SolutionRepository;
import com.study.solution.Repository.TestRepository;
import com.study.solution.Service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.study.common.Constants.Consts.USERNAME_CLAIM;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final SolutionRepository solutionRepository;
    private final TestListMapper testListMapper;
    private final TestMapper testMapper;

    @Override
    public List<TestDto> getTests(Jwt user, UUID solutionId){
        Solution solution = solutionRepository.findSolutionById(solutionId)
                .orElseThrow(() -> new SolutionNotFoundException(solutionId));


        return testListMapper.toModelList(testRepository.findAllBySolution(solution));
    }

    @Override
    public MentorTestDto getInfoAboutTest(UUID testId){
        return testMapper.toMentorDTO(testRepository.findTestById(testId)
                .orElseThrow(() -> new TestNotFoundException(testId)));
    }
}

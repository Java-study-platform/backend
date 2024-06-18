package com.study.user.Service.Impl;

import com.study.common.DTO.UserDto;
import com.study.user.DTO.UserTopDto;
import com.study.user.Entity.User;
import com.study.user.Exceptions.UserNotFoundException;
import com.study.user.Mapper.UserListMapper;
import com.study.user.Mapper.UserMapper;
import com.study.user.Repository.UserRepository;
import com.study.user.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserListMapper userListMapper;

    public UserDto getUserProfile(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с данным никнеймом не найден"));

        return userMapper.toDTO(user);
    }

    @Override
    public List<UserTopDto> getTop() {
        List<User> top = userRepository.findTop100Scorers();

        return userListMapper.toTopModelList(top);
    }
}

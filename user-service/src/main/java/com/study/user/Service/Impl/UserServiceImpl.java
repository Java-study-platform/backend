package com.study.user.Service.Impl;

import com.study.common.DTO.UserDto;
import com.study.user.Entity.User;
import com.study.user.Exceptions.UserNotFoundException;
import com.study.user.Mapper.UserMapper;
import com.study.user.Repository.UserRepository;
import com.study.user.Service.AdminService;
import com.study.user.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto getUserProfile(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с данным никнеймом не найден"));

        return userMapper.toDTO(user);
    }
}

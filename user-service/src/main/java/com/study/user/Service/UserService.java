package com.study.user.Service;

import com.study.common.DTO.UserDto;
import com.study.user.DTO.UserTopDto;

import java.util.List;

public interface UserService {
    UserDto getUserProfile(String username);

    List<UserTopDto> getTop();
}

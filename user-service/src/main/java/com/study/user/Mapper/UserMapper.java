package com.study.user.Mapper;

import com.study.common.DTO.UserDto;
import com.study.user.DTO.UserTopDto;
import com.study.user.Entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDTO(User user);

    UserTopDto toTopDto(User user);
}

package com.study.user.Mapper;

import com.study.user.DTO.UserTopDto;
import com.study.user.Entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserListMapper {
    List<UserTopDto> toTopModelList(List<User> list);
}

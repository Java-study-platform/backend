package com.study.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String keyCloakUserId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
}

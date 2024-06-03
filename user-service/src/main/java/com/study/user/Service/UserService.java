package com.study.user.Service;

import com.study.user.DTO.*;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void registerUser(UserRegistrationModel userRegistrationModel);
    TokenResponse loginUser(UserLoginModel userLoginModel);
    UserRepresentation getUserProfile(String username);
    void logoutUser(String userId);
    void assignRoles(UUID id, AssignUserRoleModel assignUserRoleModel);
}

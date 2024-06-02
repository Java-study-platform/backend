package com.study.user.Service;

import com.study.user.DTO.*;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {
    ResponseEntity<Response> registerUser(UserRegistrationModel userRegistrationModel);
    TokenResponse loginUser(UserLoginModel userLoginModel);
    ResponseEntity<UserRepresentation> getUserProfile(String username);
    ResponseEntity<Response> logoutUser(String userId);
    void assignRoles(UUID id, AssignUserRoleModel assignUserRoleModel);
}

package com.study.user.Service;

import com.study.user.DTO.Response;
import com.study.user.DTO.UserRegistrationModel;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<Response> registerUser(UserRegistrationModel userRegistrationModel);
    ResponseEntity<UserRepresentation> getUserProfile(String username);
    ResponseEntity<Response> logoutUser(String userId);
}

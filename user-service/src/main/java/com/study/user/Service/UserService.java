package com.study.user.Service;

import com.study.user.DTO.Response;
import com.study.user.DTO.UserRegistrationModel;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<Response> registerUser(UserRegistrationModel userRegistrationModel);
    ResponseEntity<UserRepresentation> getUserById(String userId);
    ResponseEntity<Response> logoutUser(String userId);
}

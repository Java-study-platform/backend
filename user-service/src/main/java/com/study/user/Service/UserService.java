package com.study.user.Service;

import com.study.user.DTO.*;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserRepresentation getUserProfile(String username);
}

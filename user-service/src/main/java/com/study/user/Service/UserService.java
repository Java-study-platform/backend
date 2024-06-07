package com.study.user.Service;

import org.keycloak.representations.idm.UserRepresentation;

public interface UserService {
    UserRepresentation getUserProfile(String username);
}

package com.study.user.Service;

import com.study.user.DTO.Response;
import com.study.user.DTO.UserRegistrationModel;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;

    public ResponseEntity<Response> registerUser(UserRegistrationModel userRegistrationModel) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setEmail(userRegistrationModel.getEmail());
        user.setUsername(userRegistrationModel.getUsername());
        user.setFirstName(userRegistrationModel.getFirstName());
        user.setLastName(userRegistrationModel.getLastName());

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userRegistrationModel.getPassword());
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        List<CredentialRepresentation> list = new ArrayList<>();
        list.add(credentialRepresentation);

        user.setCredentials(list);

        UsersResource usersResource = getUsersResourse();

        try(jakarta.ws.rs.core.Response resp = usersResource.create(user)){
            if (Objects.equals(201, resp.getStatus())){
                return new ResponseEntity<>(new Response(LocalDateTime.now(), HttpStatus.OK.value(),
                        "Пользователь успешно зарегистрирован"), HttpStatus.OK);
            }
            else{
                throw new RuntimeException();
            }
        }
    }

    private UsersResource getUsersResourse(){
        RealmResource resource = keycloak.realm(realm);
        return resource.users();
    }

    public ResponseEntity<Response> logoutUser(String userId){
        getUsersResourse().get(userId).logout();
        return new ResponseEntity<>(new Response(LocalDateTime.now(), HttpStatus.OK.value(),
                "Пользователь успешно вышел из аккаунта"), HttpStatus.OK);
    }

    public ResponseEntity<UserRepresentation> getUserById(String userId){
        return ResponseEntity.ok(getUsersResourse().get(userId).toRepresentation());
    }
}

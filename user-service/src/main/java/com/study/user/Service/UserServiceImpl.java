package com.study.user.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.user.DTO.Response;
import com.study.user.DTO.UserRegistrationModel;
import com.study.user.Entity.User;
import com.study.user.Exceptions.InternalServerException;
import com.study.user.Exceptions.UserAlreadyExistsException;
import com.study.user.Exceptions.UserNotFoundException;
import com.study.user.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.study.user.Consts.Consts.USER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${keycloak.realm}")
    private String realm;

    @Autowired
    private final Keycloak keycloak;

    private final UserRepository userRepository;

    @Transactional
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
                String userId = resp.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                UserRepresentation registeredUser = usersResource.get(userId).toRepresentation();

                User entityUser = getUser(registeredUser);

                userRepository.save(entityUser);

                return new ResponseEntity<>(new Response(LocalDateTime.now(), HttpStatus.OK.value(),
                        "Пользователь успешно зарегистрирован"), HttpStatus.OK);
            }
            else if (Objects.equals(409, resp.getStatus())){
                String errorMessage = "Пользователь с указанными данными уже существует";
                if (resp.hasEntity()) {
                    try {
                        String json = resp.readEntity(String.class);
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(json);
                        if (jsonNode.has("errorMessage")) {
                            errorMessage = jsonNode.get("errorMessage").asText();
                        }
                    } catch (Exception e) {
                        throw new InternalServerException();
                    }
                }
                throw new UserAlreadyExistsException(errorMessage);
            }
            else{
                throw new RuntimeException();
            }
        }
    }

    private static User getUser(UserRepresentation registeredUser) {
        String keycloakId = registeredUser.getId();
        String email = registeredUser.getEmail();
        String username = registeredUser.getUsername();
        String firstName = registeredUser.getFirstName();
        String lastName = registeredUser.getLastName();
        List<String> roles = new ArrayList<>();
        roles.add(USER);

        User entityUser = new User();
        entityUser.setEmail(email);
        entityUser.setKeyCloakId(keycloakId);
        entityUser.setUsername(username);
        entityUser.setFirstName(firstName);
        entityUser.setLastName(lastName);
        entityUser.setRoles(roles);

        return entityUser;
    }

    private UsersResource getUsersResourse(){
        RealmResource resource = keycloak.realm(realm);
        return resource.users();
    }

    public ResponseEntity<Response> logoutUser(String username){
        User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException("Пользователь с данным никнеймом не найден"));

        getUsersResourse().get(user.getKeyCloakId()).logout();

        return new ResponseEntity<>(new Response(LocalDateTime.now(), HttpStatus.OK.value(),
                "Пользователь успешно вышел из аккаунта"), HttpStatus.OK);
    }

    public ResponseEntity<UserRepresentation> getUserProfile(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с данным никнеймом не найден"));

        return ResponseEntity.ok(getUsersResourse().get(user.getKeyCloakId()).toRepresentation());
    }
}

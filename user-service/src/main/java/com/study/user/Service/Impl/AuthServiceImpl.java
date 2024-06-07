package com.study.user.Service.Impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.user.DTO.TokenResponse;
import com.study.user.DTO.UserLoginModel;
import com.study.user.DTO.UserRegistrationModel;
import com.study.user.Entity.User;
import com.study.user.Exceptions.BadCredentialsException;
import com.study.user.Exceptions.InternalServerException;
import com.study.user.Exceptions.UserAlreadyExistsException;
import com.study.user.Exceptions.UserNotFoundException;
import com.study.user.Repository.UserRepository;
import com.study.user.Service.AdminService;
import com.study.user.Service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.study.common.Constants.Consts.USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthzClient authzClient;
    private final AdminService adminService;

    @Override
    public void logoutUser(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с данным никнеймом не найден"));

        adminService.getUsersResourse().get(user.getKeyCloakId()).logout();
    }

    @Override
    public TokenResponse loginUser(UserLoginModel userLoginModel) {
        try {
            AccessTokenResponse response = authzClient.obtainAccessToken(userLoginModel.getLogin(), userLoginModel.getPassword());

            return new TokenResponse(
                    response.getToken(),
                    response.getExpiresIn(),
                    response.getRefreshExpiresIn(),
                    response.getRefreshToken(),
                    response.getTokenType(),
                    response.getNotBeforePolicy(),
                    response.getSessionState(),
                    response.getScope()
            );

        } catch (Exception exception) {
            throw new BadCredentialsException();
        }
    }

    @Transactional
    public void registerUser(UserRegistrationModel userRegistrationModel) {
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

        UsersResource usersResource = adminService.getUsersResourse();

        try(jakarta.ws.rs.core.Response resp = usersResource.create(user)){
            if (Objects.equals(201, resp.getStatus())){
                String userId = resp.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                UserRepresentation registeredUser = usersResource.get(userId).toRepresentation();

                User entityUser = getUser(registeredUser);

                userRepository.save(entityUser);
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
                log.info(String.valueOf(resp.getStatus()));
                log.info(resp.readEntity(String.class));
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
}

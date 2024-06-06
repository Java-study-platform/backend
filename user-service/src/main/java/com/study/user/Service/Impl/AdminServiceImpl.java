package com.study.user.Service.Impl;

import com.study.user.DTO.AssignUserRoleModel;
import com.study.user.Entity.User;
import com.study.user.Exceptions.RoleNotFoundException;
import com.study.user.Exceptions.UserNotFoundException;
import com.study.user.Repository.UserRepository;
import com.study.user.Service.AdminService;
import com.study.user.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;

    @Value("${keycloak.realm}")
    private String realm;

    @Autowired
    private final Keycloak keycloak;

    @Override
    @Transactional
    public void assignRoles(UUID id, AssignUserRoleModel assignUserRoleModel) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с данным id не найден"));

        List<RoleRepresentation> keycloakRoles = new ArrayList<>();
        assignUserRoleModel.getRoles().forEach(role -> {
            try {
                keycloakRoles.add(keycloak.realm(realm).roles().get("ROLE_"+role.toUpperCase()).toRepresentation());
            }
            catch (Exception e) {
                throw new RoleNotFoundException(role);
            }
        });

        getUsersResourse().get(user.getKeyCloakId()).roles().realmLevel().remove(
                user.getRoles().stream().map(role -> keycloak.realm(realm).roles().get("ROLE_"+role).toRepresentation()).collect(Collectors.toList())
        );
        getUsersResourse().get(user.getKeyCloakId()).roles().realmLevel().add(keycloakRoles);

        user.setRoles(assignUserRoleModel.getRoles().stream().map(String::toUpperCase).collect(Collectors.toList()));

        userRepository.save(user);
    }

    public UsersResource getUsersResourse(){
        RealmResource resource = keycloak.realm(realm);
        return resource.users();
    }
}

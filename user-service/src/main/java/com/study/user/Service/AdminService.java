package com.study.user.Service;

import com.study.user.DTO.AssignUserRoleModel;
import org.keycloak.admin.client.resource.UsersResource;

import java.util.UUID;

public interface AdminService {
    void assignRoles(UUID id, AssignUserRoleModel assignUserRoleModel);

    UsersResource getUsersResourse();
}

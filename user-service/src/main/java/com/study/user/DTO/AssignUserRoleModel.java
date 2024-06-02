package com.study.user.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignUserRoleModel {
    @NotNull(message = "Коллекция ролей не может быть пустой")
    private List<String> roles;
}

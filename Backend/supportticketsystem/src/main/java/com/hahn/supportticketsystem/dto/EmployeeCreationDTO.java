package com.hahn.supportticketsystem.dto;

import com.hahn.supportticketsystem.model.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeCreationDTO {

    @NotNull
    @Size(min = 1, max = 100)
    private String username;

    @NotNull
    @Size(min = 1, max = 255)
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;
}
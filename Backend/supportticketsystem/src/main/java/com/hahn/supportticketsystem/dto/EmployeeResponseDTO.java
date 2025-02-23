package com.hahn.supportticketsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponseDTO {
    private Long employeeId;
    private String username;
    private String role;
}
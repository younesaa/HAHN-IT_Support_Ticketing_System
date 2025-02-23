package com.hahn.supportticketsystem.utils;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.hahn.supportticketsystem.dto.EmployeeCreationDTO;
import com.hahn.supportticketsystem.model.Employee;

@Mapper
public interface EmployeeCreationMapper {
    EmployeeCreationMapper INSTANCE = Mappers.getMapper(EmployeeCreationMapper.class);

    EmployeeCreationDTO toDTO(Employee employee);
    Employee toEntity(EmployeeCreationDTO employeeCreationDTO);
}
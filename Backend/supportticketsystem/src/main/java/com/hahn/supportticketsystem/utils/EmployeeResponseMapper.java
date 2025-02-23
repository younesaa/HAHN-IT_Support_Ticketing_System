package com.hahn.supportticketsystem.utils;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.hahn.supportticketsystem.dto.EmployeeResponseDTO;
import com.hahn.supportticketsystem.model.Employee;

@Mapper
public interface EmployeeResponseMapper {
    EmployeeResponseMapper INSTANCE = Mappers.getMapper(EmployeeResponseMapper.class);

    EmployeeResponseDTO toDTO(Employee employee);
    Employee toEntity(EmployeeResponseDTO employeeResponseDTO);
}

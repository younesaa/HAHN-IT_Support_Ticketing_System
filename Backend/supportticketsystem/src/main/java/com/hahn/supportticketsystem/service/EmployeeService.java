package com.hahn.supportticketsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hahn.supportticketsystem.dto.EmployeeCreationDTO;
import com.hahn.supportticketsystem.dto.EmployeeResponseDTO;
import com.hahn.supportticketsystem.exception.UsernameAlreadyExistsException;
import com.hahn.supportticketsystem.exception.UsernameNotFoundException;
import com.hahn.supportticketsystem.model.Employee;
import com.hahn.supportticketsystem.repository.EmployeeRepository;
import com.hahn.supportticketsystem.utils.EmployeeCreationMapper;
import com.hahn.supportticketsystem.utils.EmployeeResponseMapper;

@Service
public class EmployeeService {

    public static final String EMPLOYEE_NOT_FOUND="Employee not found";
    public static final String EMPLOYEE_ALREADY_EXISTS="Employee already taken";

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

        public EmployeeResponseDTO getEmployeeByUsername(String username) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(EMPLOYEE_NOT_FOUND));
        return EmployeeResponseMapper.INSTANCE.toDTO(employee);
    }

    public EmployeeResponseDTO createEmployee(EmployeeCreationDTO employeeCreationDTO) {
        if (employeeRepository.existsByUsername(employeeCreationDTO.getUsername())) {
            throw new UsernameAlreadyExistsException(EMPLOYEE_ALREADY_EXISTS);
        }
        Employee employee = EmployeeCreationMapper.INSTANCE.toEntity(employeeCreationDTO);
        // Encode the password before saving
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        Employee createdEmployee = employeeRepository.save(employee);
        return EmployeeResponseMapper.INSTANCE.toDTO(createdEmployee);
    }

    public void deleteEmployee(String username) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(EMPLOYEE_NOT_FOUND));
        employeeRepository.delete(employee);
    }
}

package com.hahn.supportticketsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hahn.supportticketsystem.dto.EmployeeCreationDTO;
import com.hahn.supportticketsystem.dto.EmployeeResponseDTO;
import com.hahn.supportticketsystem.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/employees")
@Tag(name = "employees Controller", description = "employees management Apis | only IT_SUPPORT can access these Apis")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }



    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('IT_SUPPORT')")
    @Operation(summary = "get employee by username")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByUsername(@PathVariable String username) {
            EmployeeResponseDTO employeeResponseDTO = employeeService.getEmployeeByUsername(username);
            return ResponseEntity.ok(employeeResponseDTO);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('IT_SUPPORT')")
    @Operation(summary = "create a new employee")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@RequestBody EmployeeCreationDTO employeeCreationDTO) {
        EmployeeResponseDTO employeeResponseDTO = employeeService.createEmployee(employeeCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(employeeResponseDTO);
    }

    @PreAuthorize("hasAuthority('IT_SUPPORT')")
    @DeleteMapping("/{username}")
    @Operation(summary = "delete employee by username")
    public ResponseEntity<String> deleteEmployee(@PathVariable String username) {
        employeeService.deleteEmployee(username);
        return ResponseEntity.ok("Employee Deleted");
    }
}
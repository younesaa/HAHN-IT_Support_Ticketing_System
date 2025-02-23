package com.hahn.supportticketsystem.config.DataInitialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hahn.supportticketsystem.model.Employee;
import com.hahn.supportticketsystem.model.Role;
import com.hahn.supportticketsystem.repository.EmployeeRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
 
    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername; 

    @Value("${admin.password}")
    private String adminPassword; 

    @Autowired
    public DataInitializer(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if(employeeRepository.count() == 0) {
            Employee admin = new Employee();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.IT_SUPPORT);
            employeeRepository.save(admin);
        }
    }
}

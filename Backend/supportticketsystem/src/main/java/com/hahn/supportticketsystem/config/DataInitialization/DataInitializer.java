package com.hahn.supportticketsystem.config.DataInitialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hahn.supportticketsystem.model.AuditLog;
import com.hahn.supportticketsystem.model.Category;
import com.hahn.supportticketsystem.model.Employee;
import com.hahn.supportticketsystem.model.Priority;
import com.hahn.supportticketsystem.model.Role;
import com.hahn.supportticketsystem.model.Status;
import com.hahn.supportticketsystem.model.Ticket;
import com.hahn.supportticketsystem.repository.AuditLogRepository;
import com.hahn.supportticketsystem.repository.CommentRepository;
import com.hahn.supportticketsystem.repository.EmployeeRepository;
import com.hahn.supportticketsystem.repository.TicketRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
 
    private final EmployeeRepository employeeRepository;
    private final TicketRepository ticketRepository;


    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username}")
    private String adminUsername; 

    @Value("${admin.password}")
    private String adminPassword; 

    @Autowired
    public DataInitializer(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, TicketRepository ticketRepository, CommentRepository commentRepository, AuditLogRepository auditLogRepository) {
        this.employeeRepository = employeeRepository;
        this.ticketRepository = ticketRepository;
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

        // testing setup 
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        Employee support1 = new Employee();

        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        Ticket ticket3 = new Ticket();
        Ticket ticket4 = new Ticket();
        Ticket ticket5 = new Ticket();
        if (!employeeRepository.existsByUsername("userEmployee1") && !employeeRepository.existsByUsername("userEmployee2") && !employeeRepository.existsByUsername("userSupport1")) {
            employee1.setUsername("userEmployee1");
            employee1.setPassword(passwordEncoder.encode("userEmployee1"));
            employee1.setRole(Role.EMPLOYEE);
            employeeRepository.save(employee1);

            employee2.setUsername("userEmployee2");
            employee2.setPassword(passwordEncoder.encode("userEmployee2"));
            employee2.setRole(Role.EMPLOYEE);
            employeeRepository.save(employee2);

            support1.setUsername("userSupport1");
            support1.setPassword(passwordEncoder.encode("userSupport1"));
            support1.setRole(Role.IT_SUPPORT);
            employeeRepository.save(support1);

            ticket1.setTitle("E1ticket1");
            ticket1.setDescription("E1ticket1 description");
            ticket1.setPriority(Priority.HIGH);
            ticket1.setCategory(Category.Hardware);
            ticket1.setEmployee(employee1);
            ticket1.setStatus(Status.NEW);
            ticketRepository.save(ticket1);

            ticket2.setTitle("E1ticket2");
            ticket2.setDescription("E1ticket2 description");
            ticket2.setPriority(Priority.LOW);
            ticket2.setCategory(Category.Network);
            ticket2.setEmployee(employee1);
            ticket2.setStatus(Status.NEW);
            ticketRepository.save(ticket2);

            ticket3.setTitle("E2ticket1");
            ticket3.setDescription("E2ticket1 description");
            ticket3.setPriority(Priority.HIGH);
            ticket3.setCategory(Category.Hardware);
            ticket3.setEmployee(employee2);
            ticket3.setStatus(Status.NEW);
            ticketRepository.save(ticket3);

            ticket4.setTitle("E2ticket2");
            ticket4.setDescription("E2ticket2 description");
            ticket4.setPriority(Priority.LOW);
            ticket4.setCategory(Category.Other);
            ticket4.setEmployee(employee2);
            ticket4.setStatus(Status.NEW);
            ticketRepository.save(ticket4);

            ticket5.setTitle("E2ticket3");
            ticket5.setDescription("E2ticket3 description");
            ticket5.setPriority(Priority.MEDIUM);
            ticket5.setCategory(Category.Other);
            ticket5.setEmployee(employee2);
            ticket5.setStatus(Status.RESOLVED);
            ticketRepository.save(ticket5);
        }
    }
}

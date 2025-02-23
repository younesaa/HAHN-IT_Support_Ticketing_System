package com.hahn.supportticketsystem;

import com.hahn.supportticketsystem.dto.EmployeeCreationDTO;
import com.hahn.supportticketsystem.dto.EmployeeResponseDTO;
import com.hahn.supportticketsystem.exception.UsernameNotFoundException;
import com.hahn.supportticketsystem.exception.UsernameAlreadyExistsException;
import com.hahn.supportticketsystem.model.Employee;
import com.hahn.supportticketsystem.repository.EmployeeRepository;
import com.hahn.supportticketsystem.service.EmployeeService;
import com.hahn.supportticketsystem.utils.EmployeeResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private EmployeeResponseMapper employeeResponseMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetEmployeeByUsername_Success() {
        // Arrange
        String username = "testUsername";
        Employee employee = new Employee();
        employee.setUsername(username);

        when(employeeRepository.findByUsername(username)).thenReturn(Optional.of(employee));

        // Act
        EmployeeResponseDTO result = employeeService.getEmployeeByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(employeeRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testGetEmployeeByUsername_NotFound() {
        // Arrange
        String username = "testUsername";
        when(employeeRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> employeeService.getEmployeeByUsername(username));
        verify(employeeRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testCreateEmployee_UsernameAlreadyExists() {
        // Arrange
        EmployeeCreationDTO employeeCreationDTO = new EmployeeCreationDTO();
        employeeCreationDTO.setUsername("existingUsername");
        when(employeeRepository.existsByUsername(employeeCreationDTO.getUsername())).thenReturn(true);

        // Act & Assert
        assertThrows(UsernameAlreadyExistsException.class, () -> employeeService.createEmployee(employeeCreationDTO));
        verify(employeeRepository, times(1)).existsByUsername(employeeCreationDTO.getUsername());
        verify(employeeRepository, times(0)).save(any(Employee.class));  // Ensure save is not called
    }

    @Test
    public void testDeleteEmployee_Success() {
        // Arrange
        String username = "testUsername";
        Employee employee = new Employee();
        employee.setUsername(username);

        when(employeeRepository.findByUsername(username)).thenReturn(Optional.of(employee));

        // Act
        employeeService.deleteEmployee(username);

        // Assert
        verify(employeeRepository, times(1)).findByUsername(username);
        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    public void testDeleteEmployee_NotFound() {
        // Arrange
        String username = "testUsername";
        when(employeeRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> employeeService.deleteEmployee(username));
        verify(employeeRepository, times(1)).findByUsername(username);
        verify(employeeRepository, times(0)).delete(any(Employee.class));  // Ensure delete is not called
    }
}

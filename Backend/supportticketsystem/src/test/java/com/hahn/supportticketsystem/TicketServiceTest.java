package com.hahn.supportticketsystem;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.hahn.supportticketsystem.dto.CommentCreationDTO;
import com.hahn.supportticketsystem.dto.TicketCreationDTO;
import com.hahn.supportticketsystem.dto.TicketResponseDTO;
import com.hahn.supportticketsystem.exception.TicketNotFoundException;
import com.hahn.supportticketsystem.exception.UsernameNotFoundException;
import com.hahn.supportticketsystem.model.Comment;
import com.hahn.supportticketsystem.model.Employee;
import com.hahn.supportticketsystem.model.Role;
import com.hahn.supportticketsystem.model.Ticket;
import com.hahn.supportticketsystem.repository.AuditLogRepository;
import com.hahn.supportticketsystem.repository.CommentRepository;
import com.hahn.supportticketsystem.repository.EmployeeRepository;
import com.hahn.supportticketsystem.repository.TicketRepository;
import com.hahn.supportticketsystem.service.TicketService;

public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private CommentRepository commentRepository;  // Mocked CommentRepository

    @InjectMocks
    private TicketService ticketService;

    private Employee employee;
    private TicketCreationDTO ticketCreationDTO;
    private Ticket ticket;
    private Comment comment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock an Employee
        employee = new Employee();
        employee.setUsername("employeeName");
        employee.setRole(Role.IT_SUPPORT);

        // Mock Ticket Creation DTO
        ticketCreationDTO = new TicketCreationDTO();
        ticketCreationDTO.setTitle("Test Ticket");
        ticketCreationDTO.setDescription("Test Ticket Description");

        // Mock Ticket
        ticket = new Ticket();
        ticket.setTicketId(1L);
        ticket.setTitle("Test Ticket");
        ticket.setDescription("Test Ticket Description");
        ticket.setEmployee(employee);
        ticket.setComments(new ArrayList<Comment>());

        // Mock Comment
        comment = new Comment();
        comment.setCommentText("This is a comment");
        comment.setTicket(ticket);
        comment.setEmployee(employee);
    }

    @Test
    public void testCreateTicket_Success() {
        // Arrange
        employee.setRole(Role.EMPLOYEE);
        when(employeeRepository.findByUsername("employeeName")).thenReturn(Optional.of(employee));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Act
        TicketResponseDTO ticketResponseDTO = ticketService.createTicket(ticketCreationDTO, "employeeName");

        // Assert
        assertNotNull(ticketResponseDTO);
        assertEquals(ticket.getTicketId(), ticketResponseDTO.getTicketId());
        assertEquals("employeeName", ticketResponseDTO.getEmployeeName());

        // Verify that the ticketRepository.save method was called once
        verify(ticketRepository, times(1)).save(any(Ticket.class));

        // Verify that auditLogRepository.save was called to log the action
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    public void testCreateTicket_EmployeeNotFound() {
        // Arrange
        when(employeeRepository.findByUsername("employeeName")).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> ticketService.createTicket(ticketCreationDTO, "employeeName"));
    }

    @Test
    public void testCreateTicket_Failure_SaveTicket() {
        // Arrange
        when(employeeRepository.findByUsername("employeeName")).thenReturn(Optional.of(employee));
        when(ticketRepository.save(any(Ticket.class))).thenThrow(new RuntimeException("Database Error"));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> ticketService.createTicket(ticketCreationDTO, "employeeName"));
    }

    @Test
    public void testGetTicketById_Success() {
        // Arrange
        when(employeeRepository.findByUsername("employeeName")).thenReturn(Optional.of(employee));
        when(ticketRepository.findByTicketIdAndEmployee(1L, employee)).thenReturn(Optional.of(ticket));

        // Act
        TicketResponseDTO ticketResponseDTO = ticketService.getTicketById("employeeName", 1L);

        // Assert
        assertNotNull(ticketResponseDTO);
        assertEquals(ticket.getTicketId(), ticketResponseDTO.getTicketId());
        assertEquals("employeeName", ticketResponseDTO.getEmployeeName());
    }

    @Test
    public void testGetTicketById_TicketNotFound() {
        // Arrange
        when(employeeRepository.findByUsername("employeeName")).thenReturn(Optional.of(employee));
        when(ticketRepository.findByTicketIdAndEmployee(1L, employee)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById("employeeName", 1L));
    }

    @Test
    public void testGetAllTicketsFiltred_Success() {
        // Arrange
        when(employeeRepository.findByUsername("employeeName")).thenReturn(Optional.of(employee));
        List<Ticket> tickets = Collections.singletonList(ticket);
        when(ticketRepository.findByEmployee(employee)).thenReturn(tickets);
        when(ticketRepository.findAll()).thenReturn(tickets);

        // Act
        List<TicketResponseDTO> ticketsList = ticketService.getAllTicketsFiltred("employeeName", null);

        // Assert
        assertNotNull(ticketsList);
        assertEquals(1, ticketsList.size());
        assertEquals(ticket.getTicketId(), ticketsList.get(0).getTicketId());
    }

    @Test
    public void testUpdateTicketStatus_Success() {
        // Arrange
        when(employeeRepository.findByUsername("employeeName")).thenReturn(Optional.of(employee));
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Act
        TicketResponseDTO ticketResponseDTO = ticketService.updateTicketStatus("employeeName", 1L, "RESOLVED");

        // Assert
        assertNotNull(ticketResponseDTO);
        assertEquals(ticket.getTicketId(), ticketResponseDTO.getTicketId());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(auditLogRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateTicketStatus_TicketNotFound() {
        // Arrange
        when(employeeRepository.findByUsername("employeeName")).thenReturn(Optional.of(employee));
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(TicketNotFoundException.class, () -> ticketService.updateTicketStatus("employeeName", 1L, "OPEN"));
    }

    @Test
    public void testCreateComment_TicketNotFound() {
        // Arrange
        CommentCreationDTO commentCreationDTO = new CommentCreationDTO();
        commentCreationDTO.setCommentText("This is a test comment");

        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(TicketNotFoundException.class, () -> ticketService.createComment("employeeName", 1L, commentCreationDTO));
    }

    @Test
    public void testCreateComment_EmployeeNotFound() {
        // Arrange
        CommentCreationDTO commentCreationDTO = new CommentCreationDTO();
        commentCreationDTO.setCommentText("This is a test comment");

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(employeeRepository.findByUsername("employeeName")).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> ticketService.createComment("employeeName", 1L, commentCreationDTO));
    }
}
package com.hahn.supportticketsystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hahn.supportticketsystem.dto.CommentCreationDTO;
import com.hahn.supportticketsystem.dto.CommentResponseDTO;
import com.hahn.supportticketsystem.dto.TicketCreationDTO;
import com.hahn.supportticketsystem.dto.TicketResponseDTO;
import com.hahn.supportticketsystem.exception.TicketNotFoundException;
import com.hahn.supportticketsystem.exception.UsernameNotFoundException;
import com.hahn.supportticketsystem.model.AuditLog;
import com.hahn.supportticketsystem.model.Comment;
import com.hahn.supportticketsystem.model.Employee;
import com.hahn.supportticketsystem.model.Role;
import com.hahn.supportticketsystem.model.Status;
import com.hahn.supportticketsystem.model.Ticket;
import com.hahn.supportticketsystem.repository.AuditLogRepository;
import com.hahn.supportticketsystem.repository.EmployeeRepository;
import com.hahn.supportticketsystem.repository.TicketRepository;
import com.hahn.supportticketsystem.utils.CommentCreationMapper;
import com.hahn.supportticketsystem.utils.CommentResponseMapper;
import com.hahn.supportticketsystem.utils.TicketCreationMapper;
import com.hahn.supportticketsystem.utils.TicketResponseMapper;

import jakarta.transaction.Transactional;

@Service
public class TicketService {
    
    public static final String EMPLOYEE_NOT_FOUND="Employee not found";
    public static final String TICKET_NOT_FOUND="Ticket not found";
    public final TicketRepository ticketRepository;
    public final EmployeeRepository employeeRepository;
    public final AuditLogRepository auditLogRepository;

    private enum Actions {
        CREATE_TICKET,
        UPDATE_TICKET_STATUS,
        CREATE_COMMENT,
        DELETE_TICKET
    }

    @Autowired
    public TicketService(TicketRepository ticketRepository, EmployeeRepository employeeRepository, AuditLogRepository auditLogRepository) {
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public TicketResponseDTO createTicket(TicketCreationDTO ticketCreationDTO, String employeeName) {
        Ticket ticket = TicketCreationMapper.INSTANCE.toEntity(ticketCreationDTO);
        
        Employee employee = employeeRepository.findByUsername(employeeName)
        .orElseThrow(() -> new UsernameNotFoundException(EMPLOYEE_NOT_FOUND));
        
        ticket.setEmployee(employee);
        Ticket createdTicket = ticketRepository.save(ticket);

        AuditLog logger = new AuditLog();
        logger.setAction(Actions.CREATE_TICKET.toString());
        logger.setTicket(createdTicket);
        logger.setPerformedBy(employee);
        auditLogRepository.save(logger);

        TicketResponseDTO ticketResponseDTO = TicketResponseMapper.INSTANCE.toDTO(createdTicket);
        ticketResponseDTO.setEmployeeName(createdTicket.getEmployee().getUsername());
        return ticketResponseDTO;
    }

    public TicketResponseDTO getTicketById(String employeeName, Long ticketId) {
        Employee employee = employeeRepository.findByUsername(employeeName)
        .orElseThrow(() -> new UsernameNotFoundException(EMPLOYEE_NOT_FOUND));

        Ticket ticket = employeeName.equals("Admin") ? ticketRepository.findByTicketId(ticketId).orElseThrow(() -> new TicketNotFoundException(TICKET_NOT_FOUND)) 
        : ticketRepository.findByTicketIdAndEmployee(ticketId, employee).orElseThrow(() -> new TicketNotFoundException(TICKET_NOT_FOUND));

        List<CommentResponseDTO> comment2List = new ArrayList<>();
        TicketResponseDTO ticketResponseDTO = TicketResponseMapper.INSTANCE.toDTO(ticket);
        for(Comment comment2: ticket.getComments()){
            CommentResponseDTO commentResponseDTO2 = CommentResponseMapper.INSTANCE.toDTO(comment2);
            commentResponseDTO2.setEmployeeName(comment2.getEmployee().getUsername());
            commentResponseDTO2.setTicketId(comment2.getTicket().getTicketId());

            comment2List.add(commentResponseDTO2);
        }
        ticketResponseDTO.setTicketComments(comment2List);
        ticketResponseDTO.setEmployeeName(ticket.getEmployee().getUsername());       
        return ticketResponseDTO;
    }

    public List<TicketResponseDTO> getAllTicketsFiltred(String employeeName, String status) {
        Employee employee = employeeRepository.findByUsername(employeeName)
        .orElseThrow(() -> new UsernameNotFoundException(EMPLOYEE_NOT_FOUND));
        
        List<Ticket> tickets;
        List<TicketResponseDTO> ticketsList =new ArrayList<>();
        if (status == null) {
            tickets = employee.getRole().equals(Role.IT_SUPPORT) ? ticketRepository.findAll() : ticketRepository.findByEmployee(employee);
        } else {
            tickets = employee.getRole().equals(Role.IT_SUPPORT) ? ticketRepository.findByStatus(Status.valueOf(status)) : ticketRepository.findByEmployeeAndStatus(employee, Status.valueOf(status));
        }

        for (Ticket ticket : tickets) {
            List<CommentResponseDTO> comment2List = new ArrayList<>();
            TicketResponseDTO ticketResponseDTO = TicketResponseMapper.INSTANCE.toDTO(ticket);
            for(Comment comment2: ticket.getComments()){
                CommentResponseDTO commentResponseDTO2 = CommentResponseMapper.INSTANCE.toDTO(comment2);
                commentResponseDTO2.setEmployeeName(comment2.getEmployee().getUsername());
                commentResponseDTO2.setTicketId(comment2.getTicket().getTicketId());
    
                comment2List.add(commentResponseDTO2);
            }
            ticketResponseDTO.setTicketComments(comment2List);
            ticketsList.add(ticketResponseDTO);
        }

        return ticketsList;
    }    

    @Transactional
    public TicketResponseDTO updateTicketStatus(String employeeName,Long id, String status) {
        Employee employee = employeeRepository.findByUsername(employeeName)
        .orElseThrow(() -> new UsernameNotFoundException(EMPLOYEE_NOT_FOUND));
        
        Ticket ticket = ticketRepository.findById(id)
        .orElseThrow(() -> new TicketNotFoundException(TICKET_NOT_FOUND));
        
        ticket.setStatus(Status.valueOf(status));
        Ticket updatedTicket = ticketRepository.save(ticket);

        List<CommentResponseDTO> comment2List = new ArrayList<>();
        TicketResponseDTO ticketResponseDTO = TicketResponseMapper.INSTANCE.toDTO(updatedTicket);
        for(Comment comment2: ticket.getComments()){
            CommentResponseDTO commentResponseDTO2 = CommentResponseMapper.INSTANCE.toDTO(comment2);
            commentResponseDTO2.setEmployeeName(comment2.getEmployee().getUsername());
            commentResponseDTO2.setTicketId(comment2.getTicket().getTicketId());

            comment2List.add(commentResponseDTO2);
        }
        ticketResponseDTO.setTicketComments(comment2List);
        ticketResponseDTO.setEmployeeName(updatedTicket.getEmployee().getUsername());  

        AuditLog logger = new AuditLog();
        logger.setAction(Actions.UPDATE_TICKET_STATUS.toString());
        logger.setTicket(updatedTicket);
        logger.setPerformedBy(employee);
        auditLogRepository.save(logger);

        return ticketResponseDTO;
    }

    @Transactional
    public TicketResponseDTO createComment(String employeeName, Long id, CommentCreationDTO commentCreationDTO) {
        Ticket ticket = ticketRepository.findById(id)
        .orElseThrow(() -> new TicketNotFoundException(TICKET_NOT_FOUND));

        Employee employee = employeeRepository.findByUsername(employeeName)
        .orElseThrow(() -> new UsernameNotFoundException(EMPLOYEE_NOT_FOUND));

        Comment comment = CommentCreationMapper.INSTANCE.toEntity(commentCreationDTO);
        comment.setEmployee(employee);
        comment.setTicket(ticket);

        List<Comment> commentList = ticket.getComments();
        commentList.add(comment);
        ticket.setComments(commentList);
        Ticket updatedTicket = ticketRepository.save(ticket);

        List<CommentResponseDTO> comment2List = new ArrayList<>();
        TicketResponseDTO ticketResponseDTO = TicketResponseMapper.INSTANCE.toDTO(updatedTicket);
        for(Comment comment2: updatedTicket.getComments()){
            CommentResponseDTO commentResponseDTO2 = CommentResponseMapper.INSTANCE.toDTO(comment2);
            commentResponseDTO2.setEmployeeName(comment2.getEmployee().getUsername());
            commentResponseDTO2.setTicketId(comment2.getTicket().getTicketId());

            comment2List.add(commentResponseDTO2);
        }
        ticketResponseDTO.setTicketComments(comment2List);
        ticketResponseDTO.setEmployeeName(updatedTicket.getEmployee().getUsername());

        AuditLog logger = new AuditLog();
        logger.setAction(Actions.CREATE_COMMENT.toString());
        logger.setTicket(updatedTicket);
        logger.setPerformedBy(employee);
        auditLogRepository.save(logger);

        return ticketResponseDTO;

    }
}
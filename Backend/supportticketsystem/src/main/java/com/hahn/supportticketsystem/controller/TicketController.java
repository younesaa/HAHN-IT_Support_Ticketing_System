package com.hahn.supportticketsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hahn.supportticketsystem.dto.CommentCreationDTO;
import com.hahn.supportticketsystem.dto.TicketCreationDTO;
import com.hahn.supportticketsystem.dto.TicketResponseDTO;
import com.hahn.supportticketsystem.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/tickets")
@Tag(name = "Ticket Controller", description = "Ticket Management Apis")
public class TicketController {
    
    private final TicketService ticketService;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @Operation(summary = "Create a new ticket")
    public ResponseEntity<TicketResponseDTO> createTicket (@RequestBody TicketCreationDTO ticketCreationDTO) {
        String employeeName = authentication.getName();
        TicketResponseDTO ticketResponseDTO = ticketService.createTicket(ticketCreationDTO,employeeName);
        return new ResponseEntity<>(ticketResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{ticketId}")
    @PreAuthorize("hasAuthority('IT_SUPPORT') or hasAuthority('EMPLOYEE')")
    @Operation(summary = "get ticket by id")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable Long ticketId) {
        String employeeName = authentication.getName();
        TicketResponseDTO ticketResponseDTO = ticketService.getTicketById(employeeName, ticketId);
        return new ResponseEntity<>(ticketResponseDTO, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('IT_SUPPORT') or hasAuthority('EMPLOYEE')")
    @Operation(summary = "get all tickets  | optional filter by status")
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets(@RequestParam(required = false) String status) {
        String employeeName = authentication.getName();
        List<TicketResponseDTO> ticketsList = ticketService.getAllTicketsFiltred(employeeName, status);
        return new ResponseEntity<>(ticketsList, HttpStatus.OK);
    }

    @PutMapping("/status/{ticketId}")
    @PreAuthorize("hasAuthority('IT_SUPPORT')")
    @Operation(summary = "update ticket status")
    public ResponseEntity<TicketResponseDTO> updateTicketStatus(@PathVariable Long ticketId, @RequestParam(required = true) String status) {
        String employeeName = authentication.getName();
        TicketResponseDTO updatedTicket = ticketService.updateTicketStatus(employeeName, ticketId, status);
        return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
    }

    @PutMapping("/comments/{ticketId}")
    @PreAuthorize("hasAuthority('IT_SUPPORT')")
    @Operation(summary = "add ticket commment")
    public ResponseEntity<TicketResponseDTO> addCommentToTicket(@PathVariable Long ticketId, @RequestBody CommentCreationDTO commentCreationDTO) {
        String employeeName = authentication.getName();
        TicketResponseDTO updatedTicket = ticketService.createComment(employeeName, ticketId, commentCreationDTO);
        return new ResponseEntity<>(updatedTicket, HttpStatus.CREATED);
    }
}

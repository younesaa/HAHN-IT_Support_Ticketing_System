package com.hahn.supportticketsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hahn.supportticketsystem.dto.AuditLogDto;
import com.hahn.supportticketsystem.dto.TicketResponseDTO;
import com.hahn.supportticketsystem.model.AuditLog;
import com.hahn.supportticketsystem.service.AuditLogService;
import com.hahn.supportticketsystem.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/audit/logs")
public class AuditLogController {

     private final AuditLogService auditLogService;
    
    @Autowired
    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('IT_SUPPORT')")
    @Operation(summary = "get all creation tickets, Update statuses and add comments actions")
    public ResponseEntity<List<AuditLogDto>> getAllTickets() {
        List<AuditLogDto> auditList = auditLogService.getLogs();
        return new ResponseEntity<>(auditList, HttpStatus.OK);
    }
}

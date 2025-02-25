package com.hahn.supportticketsystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hahn.supportticketsystem.dto.AuditLogDto;
import com.hahn.supportticketsystem.model.AuditLog;
import com.hahn.supportticketsystem.repository.AuditLogRepository;


@Service
public class AuditLogService {

        public final AuditLogRepository auditLogRepository;

            @Autowired
    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public List<AuditLogDto> getLogs(){
        List<AuditLog> auditList=  auditLogRepository.findAll();
        List<AuditLogDto> auditListDto = new ArrayList<>();
        for(AuditLog logDb : auditList){
            AuditLogDto logDto = new AuditLogDto();
            logDto.setAction(logDb.getAction());
            logDto.setActionDate(logDb.getActionDate());
            logDto.setEmployeeName(logDb.getPerformedBy().getUsername());
            logDto.setTicketId(logDb.getTicket().getTicketId());
            auditListDto.add(logDto);
        }
        return auditListDto;
    }
}

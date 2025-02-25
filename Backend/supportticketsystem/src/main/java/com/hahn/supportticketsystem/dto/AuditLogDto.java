package com.hahn.supportticketsystem.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AuditLogDto {

    private String action;
    private Long ticketId;
    private String employeeName;
    private LocalDateTime actionDate;
}

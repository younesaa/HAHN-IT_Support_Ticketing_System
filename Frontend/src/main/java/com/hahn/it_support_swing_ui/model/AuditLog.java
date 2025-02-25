package com.hahn.it_support_swing_ui.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {
    
    private String action;
    private Long ticketId;
    private String employeeName;
    private String actionDate;
}

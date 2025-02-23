package com.hahn.supportticketsystem.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Audit_log")
@NoArgsConstructor
@Getter
@Setter
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false, unique = true)
    private Long logId;

    @Column(nullable = false, length = 200)
    private String action;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "performed_by", nullable = false)
    private Employee performedBy;

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate = LocalDateTime.now();
}

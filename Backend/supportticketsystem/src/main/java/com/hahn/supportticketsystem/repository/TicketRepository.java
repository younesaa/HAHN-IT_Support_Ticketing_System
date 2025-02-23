package com.hahn.supportticketsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hahn.supportticketsystem.model.Employee;
import com.hahn.supportticketsystem.model.Status;
import com.hahn.supportticketsystem.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByTicketIdAndEmployee(Long ticketId, Employee employee);
    List<Ticket> findByEmployee(Employee employee);
    List<Ticket> findByStatus(Status status);
    List<Ticket> findByEmployeeAndStatus(Employee employee, Status status);
    Optional<Ticket> findByTicketId(Long ticketId);
}

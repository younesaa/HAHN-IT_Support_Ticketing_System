package com.hahn.supportticketsystem.utils;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.hahn.supportticketsystem.dto.TicketResponseDTO;
import com.hahn.supportticketsystem.model.Ticket;

// need to add ListMAppers as well

@Mapper
public interface TicketResponseMapper {
    TicketResponseMapper INSTANCE = Mappers.getMapper(TicketResponseMapper.class);

    TicketResponseDTO toDTO(Ticket ticket);
    Ticket toEntity(TicketResponseDTO ticketResponseDTO);
}
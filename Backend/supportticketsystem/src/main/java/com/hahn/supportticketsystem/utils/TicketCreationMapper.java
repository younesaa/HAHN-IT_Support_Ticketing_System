package com.hahn.supportticketsystem.utils;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.hahn.supportticketsystem.dto.TicketCreationDTO;
import com.hahn.supportticketsystem.model.Ticket;

@Mapper
public interface TicketCreationMapper {
    TicketCreationMapper INSTANCE = Mappers.getMapper(TicketCreationMapper.class);

    TicketCreationDTO toDTO(Ticket ticket);
    Ticket toEntity(TicketCreationDTO ticketCreationDTO);
}
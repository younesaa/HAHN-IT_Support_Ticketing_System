package com.hahn.it_support_swing_ui.utils;

import java.util.List;

import com.hahn.it_support_swing_ui.model.TicketResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PairReturn{
    public List<TicketResponseDTO> listTickets;
    public int responseCode;
}
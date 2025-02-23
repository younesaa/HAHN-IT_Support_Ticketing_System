package com.hahn.supportticketsystem.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.hahn.supportticketsystem.model.Category;
import com.hahn.supportticketsystem.model.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketResponseDTO {

    private Long ticketId;
    private String title;
    private String description;
    private String priority;
    private Category category;
    private Status status;
    private LocalDateTime creationDate;
    private String employeeName;
    private List<CommentResponseDTO> ticketComments;
}

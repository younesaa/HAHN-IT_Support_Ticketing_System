package com.hahn.supportticketsystem.dto;

import com.hahn.supportticketsystem.model.Category;
import com.hahn.supportticketsystem.model.Priority;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketCreationDTO {

    @NotNull
    @Size(min = 1, max = 255)
    private String title;
    @NotNull
    @Size(min = 1, max = 255)
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Enumerated(EnumType.STRING)
    private Category category;
}

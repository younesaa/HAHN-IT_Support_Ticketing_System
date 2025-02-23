package com.hahn.supportticketsystem.dto;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDTO {

    private Long commentId;
    private String commentText;
    private Long ticketId;
    private String employeeName;
     private LocalDateTime commentDate;

}

package com.hahn.supportticketsystem.dto;



import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreationDTO {

    @NotNull
    @Size(min = 1, max = 500)
    private String commentText;

}

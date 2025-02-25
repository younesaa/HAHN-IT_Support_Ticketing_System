package  com.hahn.it_support_swing_ui.model;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDTO {

    private Long commentId;
    private String commentText;
    private String employeeName;
    private String commentDate;

}

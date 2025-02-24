package  com.hahn.it_support_swing_ui.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketResponseDTO {

    private String title;
    private String description;
    private String priority;
    private String category;
    private String status;
    private String creationDate;
    private List<CommentResponseDTO> ticketComments;
}

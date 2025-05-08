package utc.k62.cntt5.elearning.dto.classroom;

import lombok.Data;
import utc.k62.cntt5.elearning.domain.User;

@Data
public class NewClassRequest {
    private User teacher;
    private String className;
    private String subjectName;
    private String note;
}

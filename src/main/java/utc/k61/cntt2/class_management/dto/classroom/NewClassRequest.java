package utc.k61.cntt2.class_management.dto.classroom;

import lombok.Data;
import utc.k61.cntt2.class_management.domain.User;

@Data
public class NewClassRequest {
    private User teacher;
    private String className;
    private String subjectName;
    private String note;
}

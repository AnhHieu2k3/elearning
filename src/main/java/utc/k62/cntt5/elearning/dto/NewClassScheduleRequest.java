package utc.k62.cntt5.elearning.dto;

import lombok.Data;
import utc.k62.cntt5.elearning.enumeration.ClassPeriod;

import java.time.LocalDate;

@Data
public class NewClassScheduleRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private ClassPeriod periodInDay;
    private String dayInWeek;
    private Long classId;
}

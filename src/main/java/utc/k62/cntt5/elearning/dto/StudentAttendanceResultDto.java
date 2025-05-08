package utc.k62.cntt5.elearning.dto;

import lombok.Data;
import utc.k62.cntt5.elearning.enumeration.ClassPeriod;

import java.time.LocalDate;

@Data
public class StudentAttendanceResultDto {
    private LocalDate day;
    private ClassPeriod classPeriod;
    private Boolean attended;
}

package utc.k62.cntt5.elearning.dto;

import lombok.Data;
import utc.k62.cntt5.elearning.enumeration.ClassPeriod;

@Data
public class ClassPeriodInWeek {
    private ClassPeriod classPeriod;
    private String mondayClass;
    private String tuesdayClass;
    private String wednesdayClass;
    private String thursdayClass;
    private String fridayClass;
    private String saturdayClass;
    private String sundayClass;
}

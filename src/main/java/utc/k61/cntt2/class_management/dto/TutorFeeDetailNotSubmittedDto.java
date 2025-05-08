package utc.k61.cntt2.class_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TutorFeeDetailNotSubmittedDto {
    private String firstName;
    private String surname;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private Long id;
    private String className;
    private Long feeNotSubmitted;
    private String note;
    private Integer year;
    private Integer month;
}

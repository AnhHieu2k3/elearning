package utc.k62.cntt5.elearning.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utc.k62.cntt5.elearning.domain.User;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentDto {
    private String userNumber;
    private String firstName;
    private String surname;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate dob;
    private Long id;
    private String className;
    private Long feeNotSubmitted;
    private String note;

    public static StudentDto mapToStudentDto(User user) {
        StudentDto studentDto = new StudentDto();
        studentDto.setUserNumber(user.getUserNumber());
        studentDto.setFirstName(user.getFirstName());
        studentDto.setSurname(user.getSurname());
        studentDto.setLastName(user.getLastName());
        studentDto.setEmail(user.getEmail());
        studentDto.setPhone(user.getPhone());
        studentDto.setAddress(user.getAddress());
        return studentDto;
    }
}

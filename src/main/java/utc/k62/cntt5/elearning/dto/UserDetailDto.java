package utc.k62.cntt5.elearning.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import utc.k62.cntt5.elearning.domain.User;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserDetailDto {
    private String userNumber;
    private String username;
    private String email;
    private String phone;
    private String firstName;
    private String surname;
    private String lastName;
    private LocalDate dob;
    private String accountType;
    private String role;

    public UserDetailDto(User user) {
        this.userNumber = user.getUserNumber();
        this.username = user.getUsername();
        this.email= user.getEmail();
        this.phone = user.getPhone();
        this.firstName = user.getFirstName();
        this.surname= user.getSurname();
        this.lastName = user.getLastName();
        this.dob = user.getDob();
        this.accountType = user.getRole();
        this.role = user.getRole();
    }
}

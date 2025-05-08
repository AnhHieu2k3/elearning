package utc.k62.cntt5.elearning.dto.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpRequest {
    @NotBlank(message = "username must not be empty!")
    @Size(min = 3, max = 15)
    private String userNumber;

    @Email
    @NotBlank(message = "email must not be empty!")
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank(message = "password length must be greater 6!")
    @Size(min = 6, max = 20)
    private String password;

    @NotBlank(message = "firstname must not be empty")
    private String firstName;

    @NotBlank(message = "surname must not be empty")
    private String surname;

    @NotBlank(message = "lastname must not be empty")
    private String lastName;

    private LocalDate dob;
}

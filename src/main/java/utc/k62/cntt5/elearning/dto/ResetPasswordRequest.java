package utc.k62.cntt5.elearning.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResetPasswordRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String code;
    @NotBlank
    private String newPassword;
}

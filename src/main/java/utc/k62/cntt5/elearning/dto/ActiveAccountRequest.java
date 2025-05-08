package utc.k62.cntt5.elearning.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActiveAccountRequest {
    private String email;
    private String code;
}

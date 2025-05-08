package utc.k62.cntt5.elearning.dto;

import lombok.Data;

@Data
public class EmailDetail {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}

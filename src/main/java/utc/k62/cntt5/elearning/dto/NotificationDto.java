package utc.k62.cntt5.elearning.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private String content;
    private String type;
    private String fromUserNumber;
    private String toUserNumber;
    private Boolean isRead;
}

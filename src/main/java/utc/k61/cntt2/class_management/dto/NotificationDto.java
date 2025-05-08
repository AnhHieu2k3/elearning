package utc.k61.cntt2.class_management.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private String content;
    private String type;
    private String fromUserNumber;
    private String toUserNumber;
    private Boolean isRead;
}

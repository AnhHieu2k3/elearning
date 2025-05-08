package utc.k62.cntt5.elearning.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification extends BaseEntity {
    private String content;
    private String type;
    private String fromUserNumber;
    private String toUserNumber;
    private Boolean isRead = false;
}

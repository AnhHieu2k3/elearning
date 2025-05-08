package utc.k62.cntt5.elearning.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Video extends BaseEntity {
    private String className;
    private String title;
    private String videoUrl;
    private String thumbnailUrl;
}

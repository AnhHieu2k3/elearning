package utc.k61.cntt2.class_management.domain;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Video extends BaseEntity {
    private String className;
    private String title;
    private String videoUrl;
    private String thumbnailUrl;
}

package utc.k62.cntt5.elearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "class_document")
public class ClassDocument extends BaseEntity {

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document_link")
    private String documentLink;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
}

package utc.k62.cntt5.elearning.dto;

import lombok.Data;

@Data
public class ExamScoreDto {
    private Long id;
    private String name;
    private String email;
    private String dob;
    private Double score;
}

package utc.k62.cntt5.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utc.k62.cntt5.elearning.domain.ExamScore;

import java.util.List;

@Repository
public interface ExamScoreRepository extends JpaRepository<ExamScore, Long> {
    List<ExamScore> findAllByIdIn(List<Long> examScoreIds);
}

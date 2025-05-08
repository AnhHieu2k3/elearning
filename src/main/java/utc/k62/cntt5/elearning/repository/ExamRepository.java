package utc.k62.cntt5.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utc.k62.cntt5.elearning.domain.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
}

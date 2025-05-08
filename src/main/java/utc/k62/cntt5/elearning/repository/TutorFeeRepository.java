package utc.k62.cntt5.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import utc.k62.cntt5.elearning.domain.TutorFee;

import java.util.Optional;

@Repository
public interface TutorFeeRepository extends JpaRepository<TutorFee, Long>, JpaSpecificationExecutor<TutorFee> {
    Optional<TutorFee> findFirstByClassroomIdAndYearAndMonth(Long classId, Integer month, Integer year);
}

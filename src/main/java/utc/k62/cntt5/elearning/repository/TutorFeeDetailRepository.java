package utc.k62.cntt5.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import utc.k62.cntt5.elearning.domain.TutorFeeDetail;

@Repository
public interface TutorFeeDetailRepository extends JpaRepository<TutorFeeDetail, Long>, JpaSpecificationExecutor<TutorFeeDetail> {
}

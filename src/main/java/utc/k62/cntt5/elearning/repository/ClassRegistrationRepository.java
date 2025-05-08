package utc.k62.cntt5.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utc.k62.cntt5.elearning.domain.ClassRegistration;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ClassRegistrationRepository extends JpaSpecificationExecutor<ClassRegistration>,
        JpaRepository<ClassRegistration, Long> {
    List<ClassRegistration> findAllByClassroomIdOrderByLastNameAsc(Long classId);

    List<ClassRegistration> findAllByStudentId(Long id);

    List<ClassRegistration> findAllByEmail(String email);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM class_registration WHERE id = :studentId")
    void deleteById(@Param("studentId") Long documentId);
}

package utc.k62.cntt5.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utc.k62.cntt5.elearning.domain.ClassAttendance;

import java.util.List;

@Repository
public interface ClassAttendanceRepository extends JpaRepository<ClassAttendance, Long> {
    List<ClassAttendance> findAllByClassScheduleId(Long classScheduleId);


    List<ClassAttendance> findAllByIdIn(List<Long> attendanceIds);

    void deleteAllByClassScheduleId(Long scheduleId);

    void deleteAllByClassRegistrationId(Long studentId);
}

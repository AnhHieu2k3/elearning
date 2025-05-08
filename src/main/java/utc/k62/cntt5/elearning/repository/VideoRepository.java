package utc.k62.cntt5.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import utc.k62.cntt5.elearning.domain.Video;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findAllByClassName(String className);
}

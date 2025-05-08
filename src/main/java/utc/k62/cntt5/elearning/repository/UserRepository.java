package utc.k62.cntt5.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utc.k62.cntt5.elearning.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserNumber(String userNumber);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String userName);

    Optional<User> findFirstByEmail(String email);

    List<User> findAllByEmailIn(List<String> emails);

    Optional<User> findByUserNumber(String userNumber);
}

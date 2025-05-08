package utc.k61.cntt2.class_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utc.k61.cntt2.class_management.domain.User;

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

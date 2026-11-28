package bus.authservice.repositories;

import bus.authservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByEmailIgnoreCase(String email);
}

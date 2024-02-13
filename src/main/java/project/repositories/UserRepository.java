package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findById(Long id);
     Optional<User> findByEmailAndPassword(String email, String password);
     User findByEmail(String email);
}

package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.Company;
import project.entities.User;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
     Optional<Company> findById(Long id);
     Company findByName(String name);
     Company findByUser(User user);
}
